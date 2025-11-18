package br.edu.fiec.MapeamentoDeSaude.features.user.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.firebase.models.dto.FcmTokenRequest;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.*;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.*;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.AdminRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.PacienteRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.UbsAdminRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.UserRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
import br.edu.fiec.MapeamentoDeSaude.utils.PasswordEncryptor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor // Injeta todos os repositórios via construtor
public class UserServiceImpl implements UserService, UserDetailsService {

    // Todos os repositórios necessários
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final UbsAdminRepository ubsAdminRepository;
    private final PacienteRepository pacienteRepository;

    @Override
    public User save(User user) {
        // Garante que a senha só seja criptografada se não for uma atualização sem mudança de senha
        if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
            user.setPassword(PasswordEncryptor.getInstance().encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    /**
     * Implementação completa do /me
     * Verifica o tipo de usuário e busca os dados no repositório correto.
     */
    @Override
    public MyUserDto getMe(User user) {
        UserLevel userLevel = user.getAccessLevel();
        MyUserDto myUserDto = new MyUserDto();

        if (UserLevel.ADMIN.equals(userLevel)) {
            Admin admin = adminRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Perfil Admin não encontrado!"));
            myUserDto.setTipo("ADMIN");
            // Adicione aqui outros campos específicos do Admin se houver
            // myUserDto.setCargo(admin.getCargo());
        } else if (UserLevel.UBSADMIN.equals(userLevel)) {
            UbsAdmin ubsAdmin = ubsAdminRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Perfil UbsAdmin não encontrado!"));
            myUserDto.setTipo("UBS_ADMIN");
            myUserDto.setCnpj(ubsAdmin.getCnpj());
            myUserDto.setNomeDaEmpresa(ubsAdmin.getNomeDaUbs());
        } else { // O padrão é USER (Paciente)
            Paciente paciente = pacienteRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Perfil Paciente não encontrado!"));
            myUserDto.setTipo("PACIENTE");
            // Adicione aqui outros campos específicos do Paciente
            // myUserDto.setCpf(paciente.getCpf());
        }

        // Campos comuns a todos os tipos de usuário
        myUserDto.setNome(user.getName());
        myUserDto.setEmail(user.getEmail());
        myUserDto.setPicture(user.getPicture());
        myUserDto.setLatitude(user.getLatitude());
        myUserDto.setLongitude(user.getLongitude());
        return myUserDto;
    }

    // Função auxiliar para não repetir código
    private void checkIfUserExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }
    }

    @Override
    public CreatedUserResponseDto saveAdmin(RegisterAdminDto dto) {
        checkIfUserExists(dto.getEmail());
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());// A senha será criptografada no método save()
        user.setCpf(dto.getCpf());
        user.setTelefone(dto.getTelefone());
        user.setAccessLevel(UserLevel.ADMIN);
        user.setState(RegisterState.PROFILE_CREATED);
        User savedUser = save(user);

        Admin admin = new Admin();
        admin.setUser(savedUser);
        admin.setCargo(dto.getCargo());
        Admin savedAdmin = adminRepository.save(admin);

        return CreatedUserResponseDto.builder()
                .userId(savedUser.getId().toString())
                .id(savedAdmin.getId().toString()) // ID do perfil Admin
                .build();
    }

    @Override
    public CreatedUserResponseDto saveUbsAdmin(RegisterUbsAdminDto dto) {
        checkIfUserExists(dto.getEmail());
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCpf(dto.getCpf());
        user.setTelefone(dto.getTelefone());
        user.setAccessLevel(UserLevel.UBSADMIN);
        user.setState(RegisterState.PROFILE_CREATED);
        User savedUser = save(user);

        UbsAdmin ubsAdmin = new UbsAdmin();
        ubsAdmin.setUser(savedUser);
        ubsAdmin.setCnpj(dto.getCnpj());
        ubsAdmin.setNomeDaUbs(dto.getNomeDaUbs());
        UbsAdmin savedUbsAdmin = ubsAdminRepository.save(ubsAdmin);

        return CreatedUserResponseDto.builder()
                .userId(savedUser.getId().toString())
                .id(savedUbsAdmin.getId().toString()) // ID do perfil UbsAdmin
                .build();
    }

    @Override
    public CreatedUserResponseDto savePaciente(RegisterPacienteDto dto) {
        checkIfUserExists(dto.getEmail());
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setCpf(dto.getCpf());
        user.setTelefone(dto.getTelefone());
        user.setAccessLevel(UserLevel.USER);
        user.setState(RegisterState.PROFILE_CREATED);
        User savedUser = save(user);

        Paciente paciente = new Paciente();
        paciente.setUser(savedUser);
        paciente.setCpf(dto.getCpf());
        paciente.setCidade(dto.getCidade());
        paciente.setCep(dto.getCep());
        Paciente savedPaciente = pacienteRepository.save(paciente);

        return CreatedUserResponseDto.builder()
                .userId(savedUser.getId().toString())
                .id(savedPaciente.getId().toString()) // ID do perfil Paciente
                .build();
    }

    // Métodos restantes que você já tinha
    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setAccessLevel(updatedUser.getAccessLevel());
            user.setPicture(updatedUser.getPicture());
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(updatedUser.getPassword()); // A senha será criptografada no save()
            }
            return save(user); // Reutiliza o save para criptografar se necessário
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    /**
     * Busca o usuário e atualiza seu fcmToken.
     * @param userId O ID do usuário logado.
     * @param request O DTO contendo o novo token FCM.
     * @return O User atualizado.
     * @throws RuntimeException Se o usuário não for encontrado.
     */

    public User updateFcmToken(UUID userId, FcmTokenRequest request) {

        // 1. Busca o usuário pelo ID
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + userId));

        System.out.println(userId);
        // 2. Atualiza o atributo fcmToken
        user.setFcmToken(request.getFcmToken());

        // 3. Salva a alteração (o @Transactional garante que a persistência ocorra)
        return userRepository.save(user);
    }

    @Override
    @Transactional // quero tudo ou nada
    public void createUsers(InputStream inputStream) {


        List<UserCsvRepresentation> users = new ArrayList<>();
        try (Reader reader = new InputStreamReader(inputStream)) {

            // Create a CsvToBean object from the Reader
            CsvToBean<UserCsvRepresentation> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(UserCsvRepresentation.class) // Specify the target bean class
                    .withIgnoreLeadingWhiteSpace(true) // Clean up any extra spaces
                    .withSkipLines(0) // Skips the header row if present
                    .build();

            // Parse the data and return a List of beans
            users = csvToBean.parse();
        } catch (Exception e) {
            // Handle IO or CSV parsing exceptions
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }

        try {

            for (UserCsvRepresentation csvUser : users) {
                User user = new User();
                user.setEmail(csvUser.getEmail());
                user.setName(csvUser.getName());
                user.setPassword(PasswordEncryptor.encrypt(csvUser.getPassword()));
                UserLevel level = UserLevel.valueOf("ROLE_" + csvUser.getAccesLevel());
                user.setAccessLevel(level);
                save(user);
                switch (level) {
                    case UserLevel.USER:
                        Paciente paciente = new Paciente();

                        paciente.setUser(user);
                        paciente.setCpf(csvUser.getCpf());
                        paciente.setCidade(csvUser.getCidade());
                        paciente.setCep(csvUser.getCep());
                        pacienteRepository.save(paciente);
                        break;
                    case UserLevel.UBSADMIN:
                        UbsAdmin ubsAdmin = new UbsAdmin();

                        ubsAdmin.setUser(user);
                        ubsAdmin.setCnpj(csvUser.getCnpj());
                        ubsAdmin.setNomeDaUbs(csvUser.getNomeDaUbs());
                        ubsAdminRepository.save(ubsAdmin);
                        break;

                    default:
                        break;

                }

            }
        }catch (Exception ex){
            throw new RuntimeException("Failed to parse CSV file: " + ex.getMessage(), ex);

        }


    }
}
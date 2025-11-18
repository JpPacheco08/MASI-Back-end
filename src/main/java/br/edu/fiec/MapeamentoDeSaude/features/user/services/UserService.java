package br.edu.fiec.MapeamentoDeSaude.features.user.services;

import br.edu.fiec.MapeamentoDeSaude.features.firebase.models.dto.FcmTokenRequest;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.CreatedUserResponseDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.MyUserDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.RegisterAdminDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.RegisterPacienteDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.dto.RegisterUbsAdminDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    // Métodos básicos que você já tinha
    User save(User user);
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    User update(UUID id, User updatedUser);
    void deleteById(UUID id);

    // Método para a rota /me, que busca os dados do perfil
    MyUserDto getMe(User user);

    // Métodos novos para criar cada tipo de usuário específico
    CreatedUserResponseDto saveAdmin(RegisterAdminDto registerAdminDto);
    CreatedUserResponseDto saveUbsAdmin(RegisterUbsAdminDto registerUbsAdminDto);
    CreatedUserResponseDto savePaciente(RegisterPacienteDto registerPacienteDto);
    User updateFcmToken(UUID userId, FcmTokenRequest request);
    void createUsers(InputStream inputStream);
}
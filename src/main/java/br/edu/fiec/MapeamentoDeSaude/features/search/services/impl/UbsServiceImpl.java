package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.*;
import br.edu.fiec.MapeamentoDeSaude.utils.PasswordEncryptor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UbsServiceImpl implements UbsService {

    private final UbsRepository ubsRepository;

    @Override
    public Ubs createUbs(UbsDTO ubsDto) {
        Ubs ubs = new Ubs();
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());
        ubs.setLatitude(ubsDto.getLatitude());
        ubs.setLongitude(ubsDto.getLongitude());
        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs getUbsByName(String name) {
        return ubsRepository.findByNomeUbs(name)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada com o nome: " + name));
    }

    @Override
    public Optional<Ubs> getById(UUID uuid) {
        return ubsRepository.findById(uuid);
    }

    @Override
    public List<UbsDTO> getAllUbs() {
        List<Ubs> ubs = ubsRepository.findAll();
        return ubs.stream().map(UbsDTO::convertFromUbs).toList();
    }

    @Override
    public Ubs updateUbs(String name, UbsDTO ubsDto) {
        Ubs ubs = getUbsByName(name);
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setTelefone(ubsDto.getTelefone());
        ubs.setLatitude(ubsDto.getLatitude());
        ubs.setLongitude(ubsDto.getLongitude());
        return ubsRepository.save(ubs);
    }

    @Override
    @Transactional // quero tudo ou nada
    public void createAllUbs(InputStream inputStream) {


        List<UbsCsvRepresentation> allUbs = new ArrayList<>();
        try (Reader reader = new InputStreamReader(inputStream)) {

            // Create a CsvToBean object from the Reader
            CsvToBean<UbsCsvRepresentation> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(UserCsvRepresentation.class) // Specify the target bean class
                    .withIgnoreLeadingWhiteSpace(true) // Clean up any extra spaces
                    .withSkipLines(0) // Skips the header row if present
                    .build();

            // Parse the data and return a List of beans
            allUbs = csvToBean.parse();
        } catch (Exception e) {
            // Handle IO or CSV parsing exceptions
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage(), e);
        }

        try {

            for (UbsCsvRepresentation csvUbs : allUbs) {
                Ubs ubs = new Ubs();
                ubs.setNomeUbs(csvUbs.getNomeUbs());
                ubs.setCep(csvUbs.getCep());
                ubs.setLogradouro(csvUbs.getLogradouro());
                ubs.setComplemento(csvUbs.getComplemento());
                ubs.setTelefone(csvUbs.getTelefone());
                ubs.setId(csvUbs.getIdUbs());
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
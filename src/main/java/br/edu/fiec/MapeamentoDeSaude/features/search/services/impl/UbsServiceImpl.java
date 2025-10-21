package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<Ubs> getAllUbs() {
        return ubsRepository.findAll();
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
    public void deleteUbs(String name) {
        Ubs ubs = getUbsByName(name);
        ubsRepository.deleteById(ubs.getId());
    }
}
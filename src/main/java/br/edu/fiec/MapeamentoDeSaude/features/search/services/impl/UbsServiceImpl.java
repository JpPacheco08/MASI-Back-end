package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.UbsRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UbsServiceImpl implements UbsService {

    private final UbsRepository ubsRepository;

    @Override
    public Ubs createUbs(UbsDTO ubsDto) {
        Ubs ubs = new Ubs();
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setEndereco(ubsDto.getEndereco());
        ubs.setTelefone(ubsDto.getTelefone());
        return ubsRepository.save(ubs);
    }

    @Override
    public Ubs getUbsByName(String name) {
        return ubsRepository.findByNomeUbs(name)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada com o nome: " + name));
    }

    @Override
    public List<Ubs> getAllUbs() {
        return ubsRepository.findAll();
    }

    @Override
    public Ubs updateUbs(String name, UbsDTO ubsDto) {
        Ubs ubs = getUbsByName(name);
        ubs.setNomeUbs(ubsDto.getNomeUbs());
        ubs.setEndereco(ubsDto.getEndereco());
        ubs.setTelefone(ubsDto.getTelefone());
        return ubsRepository.save(ubs);
    }

    @Override
    public void deleteUbs(String name) {
        Ubs ubs = getUbsByName(name);
        ubsRepository.deleteById(ubs.getId());
    }
}
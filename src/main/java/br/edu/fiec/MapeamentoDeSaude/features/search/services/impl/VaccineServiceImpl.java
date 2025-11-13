package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.VaccineDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.VaccineRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.VaccineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class VaccineServiceImpl implements VaccineService {

    private final VaccineRepository vaccineRepository;

    @Override
    public Vaccine createVaccine(VaccineDTO vaccineDto) {
        Vaccine vaccine = new Vaccine();
        vaccine.setCodigoVacina(vaccineDto.getCodigoVacina());
        vaccine.setNomeVacina(vaccineDto.getNomeVacina());
        vaccine.setDescVacina(vaccineDto.getDescVacina());
        vaccine.setTratamento(vaccineDto.getTratamento());
        vaccine.setFaixaEtaria(vaccineDto.getFaixaEtaria());
        vaccine.setIntervaloEntreDoses(vaccineDto.getIntervaloEntreDoses());
        vaccine.setNumeroDoses(vaccine.getNumeroDoses());
        vaccine.setQuantidadeEstoque(vaccineDto.getQuantidadeEstoque());
        vaccine.setLote(vaccineDto.getLote());
        vaccine.setDataFabricacao(vaccineDto.getDataFabricacao());
        vaccine.setValidade(vaccineDto.getValidade());
        return vaccineRepository.save(vaccine);
    }

    @Override
    public Vaccine getVaccineByName(String name) {
        return vaccineRepository.findByNomeVacina(name)
                .orElseThrow(() -> new RuntimeException("Vacina não encontrada com o nome: " + name));
    }

    @Override
    public List<Vaccine> getAllVaccines() {
        return vaccineRepository.findAll();
    }

    @Override
    public Vaccine updateVaccine(String name, VaccineDTO vaccineDto) {
        Vaccine vaccine = getVaccineByName(name);
        vaccine.setCodigoVacina(vaccineDto.getCodigoVacina());
        vaccine.setNomeVacina(vaccineDto.getNomeVacina());
        vaccine.setDescVacina(vaccineDto.getDescVacina());
        vaccine.setTratamento(vaccineDto.getTratamento());
        vaccine.setFaixaEtaria(vaccineDto.getFaixaEtaria());
        vaccine.setIntervaloEntreDoses(vaccineDto.getIntervaloEntreDoses());
        vaccine.setNumeroDoses(vaccine.getNumeroDoses());
        vaccine.setQuantidadeEstoque(vaccineDto.getQuantidadeEstoque());
        vaccine.setLote(vaccineDto.getLote());
        vaccine.setDataFabricacao(vaccineDto.getDataFabricacao());
        vaccine.setValidade(vaccineDto.getValidade());
        return vaccineRepository.save(vaccine);
    }

    @Override
    public void deleteVaccine(UUID uuid) {
        vaccineRepository.deleteById(uuid);
    }
}
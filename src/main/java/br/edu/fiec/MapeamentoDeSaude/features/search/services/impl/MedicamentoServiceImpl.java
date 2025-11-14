package br.edu.fiec.MapeamentoDeSaude.features.search.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.MedicamentoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Medicamento;
import br.edu.fiec.MapeamentoDeSaude.features.search.repositories.MedicamentoRepository;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.MedicamentoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MedicamentoServiceImpl implements MedicamentoService {

    private MedicamentoRepository medicamentoRepository;

    @Override
    public Medicamento createMedicamento(MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = new Medicamento();
        medicamento.setCodigoMedicamento(medicamentoDTO.getCodigoMedicamento());
        medicamento.setNome(medicamentoDTO.getNome());
        medicamento.setPrincipio_ativo(medicamentoDTO.getPrincipio_ativo());
        medicamento.setTipo(medicamentoDTO.getTipo());
        medicamento.setQuantidadeEstoque(medicamentoDTO.getQuantidadeEstoque());
        medicamento.setLote(medicamentoDTO.getLote());
        medicamento.setValidade(medicamentoDTO.getValidade());
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public Medicamento getMedicamentoByNome(String nomeMedicamento) {
        return medicamentoRepository.getMedicamentoByNome(nomeMedicamento).orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o nome: " + nomeMedicamento));
    }

    @Override
    public Optional<Medicamento> getById(UUID uuid) {
        return medicamentoRepository.findById(uuid);
    }

    @Override
    public List<Medicamento> getAll() {
        return medicamentoRepository.findAll();
    }

    @Override
    public Medicamento updateMedicamento(String name, MedicamentoDTO medicamentoDTO) {
        Medicamento medicamento = getMedicamentoByNome(name);
        medicamento.setCodigoMedicamento(medicamentoDTO.getCodigoMedicamento());
        medicamento.setNome(medicamentoDTO.getNome());
        medicamento.setPrincipio_ativo(medicamentoDTO.getPrincipio_ativo());
        medicamento.setTipo(medicamentoDTO.getTipo());
        medicamento.setQuantidadeEstoque(medicamentoDTO.getQuantidadeEstoque());
        medicamento.setLote(medicamentoDTO.getLote());
        medicamento.setValidade(medicamentoDTO.getValidade());
        return medicamentoRepository.save(medicamento);
    }

    @Override
    public void deleteMedicamento(UUID uuid) {
        medicamentoRepository.deleteById(uuid);
    }
}

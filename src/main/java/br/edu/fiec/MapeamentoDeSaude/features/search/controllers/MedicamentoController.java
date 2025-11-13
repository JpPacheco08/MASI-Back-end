package br.edu.fiec.MapeamentoDeSaude.features.search.controllers;


import br.edu.fiec.MapeamentoDeSaude.features.search.dto.MedicamentoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Medicamento;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.MedicamentoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/medicamento")
@AllArgsConstructor
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @PostMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Medicamento> createMedicamento(@RequestBody MedicamentoDTO medicamentoDTO) {
        return new ResponseEntity<>(medicamentoService.createMedicamento(medicamentoDTO), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<Medicamento> getMedicamentoByNome(@PathVariable String name) {
        return new ResponseEntity<>(medicamentoService.getMedicamentoByNome(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Optional<Medicamento>> getById(@RequestParam UUID id) {
        return new ResponseEntity<>(medicamentoService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<List<Medicamento>> getAllMedicamentos(){
        return new ResponseEntity<>(medicamentoService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Medicamento> updateMedicamento(@PathVariable String name, @RequestBody MedicamentoDTO medicamentoDTO) {
        return new ResponseEntity<>(medicamentoService.updateMedicamento(name, medicamentoDTO), HttpStatus.OK);
    }
}

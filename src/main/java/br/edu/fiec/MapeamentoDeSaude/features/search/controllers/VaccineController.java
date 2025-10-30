package br.edu.fiec.MapeamentoDeSaude.features.search.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.VaccineDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.VaccineService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/vaccines")
@AllArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Vaccine> createVaccine(@RequestBody VaccineDTO vaccineDto) {
        return new ResponseEntity<>(vaccineService.createVaccine(vaccineDto), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<Vaccine> getVaccineByName(@PathVariable String name) {
        return ResponseEntity.ok(vaccineService.getVaccineByName(name));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<List<Vaccine>> getAllVaccines() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Vaccine> updateVaccine(@PathVariable String name, @RequestBody VaccineDTO vaccineDto) {
        return ResponseEntity.ok(vaccineService.updateVaccine(name, vaccineDto));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Void> deleteVaccine(@PathVariable String name) {
        vaccineService.deleteVaccine(name);
        return ResponseEntity.noContent().build();
    }
}
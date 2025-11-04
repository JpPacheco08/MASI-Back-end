package br.edu.fiec.MapeamentoDeSaude.features.search.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.VaccineDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Vaccine;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.VaccineService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/vaccines")
@AllArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;

    @PostMapping
    public ResponseEntity<Vaccine> createVaccine(@RequestBody VaccineDTO vaccineDto) {
        return new ResponseEntity<>(vaccineService.createVaccine(vaccineDto), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Vaccine> getVaccineByName(@PathVariable String name) {
        return ResponseEntity.ok(vaccineService.getVaccineByName(name));
    }

    @GetMapping
    public ResponseEntity<List<Vaccine>> getAllVaccines() {
        return ResponseEntity.ok(vaccineService.getAllVaccines());
    }

    @PutMapping("/{name}")
    public ResponseEntity<Vaccine> updateVaccine(@PathVariable String name, @RequestBody VaccineDTO vaccineDto) {
        return ResponseEntity.ok(vaccineService.updateVaccine(name, vaccineDto));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteVaccine(@PathVariable UUID uuid) {
        vaccineService.deleteVaccine(uuid);
        return ResponseEntity.noContent().build();
    }
}
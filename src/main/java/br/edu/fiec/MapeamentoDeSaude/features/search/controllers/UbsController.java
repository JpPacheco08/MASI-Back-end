package br.edu.fiec.MapeamentoDeSaude.features.search.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.EnderecoDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDistanciaDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/api/ubs")
@AllArgsConstructor
public class UbsController {

    private final UbsService ubsService;

    // 👇 Endpoint para a funcionalidade da foto (Lista Ordenada por Distância)
    @PostMapping("/proximas")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<UbsDistanciaDTO>> findUbsProximas(@Valid @RequestBody EnderecoDTO enderecoDTO) {
        return ResponseEntity.ok(ubsService.findUbsMaisProximas(enderecoDTO));
    }

    // --- Outros Endpoints (Mantidos) ---
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Ubs> createUbs(@RequestBody UbsDTO ubsDto) {
        return new ResponseEntity<>(ubsService.createUbs(ubsDto), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Ubs>> getAllUbs() {
        return ResponseEntity.ok(ubsService.getAllUbs());
    }

    @GetMapping("/{name}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Ubs> getUbsByName(@PathVariable String name) {
        return ResponseEntity.ok(ubsService.getUbsByName(name));
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Ubs> updateUbs(@PathVariable String name, @RequestBody UbsDTO ubsDto) {
        return ResponseEntity.ok(ubsService.updateUbs(name, ubsDto));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Void> deleteUbs(@PathVariable String name) {
        ubsService.deleteUbs(UUID.fromString(name));
        return ResponseEntity.noContent().build();
    }
}
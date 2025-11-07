package br.edu.fiec.MapeamentoDeSaude.features.search.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.search.dto.UbsDTO;
import br.edu.fiec.MapeamentoDeSaude.features.search.model.Ubs;
import br.edu.fiec.MapeamentoDeSaude.features.search.services.UbsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/ubs")
@AllArgsConstructor
public class UbsController {

    private final UbsService ubsService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Ubs> createUbs(@RequestBody UbsDTO ubsDto) {
        return new ResponseEntity<>(ubsService.createUbs(ubsDto), HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<Ubs> getUbsByName(@PathVariable String name) {
        return ResponseEntity.ok(ubsService.getUbsByName(name));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public ResponseEntity<List<Ubs>> getAllUbs() {
        return ResponseEntity.ok(ubsService.getAllUbs());
    }

    @PutMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Ubs> updateUbs(@PathVariable String name, @RequestBody UbsDTO ubsDto) {
        return ResponseEntity.ok(ubsService.updateUbs(name, ubsDto));
    }

    @DeleteMapping("/{name}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'UBSADMIN')")
    public ResponseEntity<Void> deleteUbs(@PathVariable String name) {
       // ubsService.deleteUbs(name);
        return ResponseEntity.noContent().build();
    }
}
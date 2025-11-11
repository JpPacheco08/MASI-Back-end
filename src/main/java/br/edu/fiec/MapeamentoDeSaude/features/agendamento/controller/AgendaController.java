package br.edu.fiec.MapeamentoDeSaude.features.agendamento.controller;

import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaRequestDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.dto.AgendaResponseDTO;
import br.edu.fiec.MapeamentoDeSaude.features.agendamento.service.AgendaService;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.UserLevel;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendas")
@AllArgsConstructor
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    public Object criaAgenda(@RequestBody AgendaRequestDTO agendaRequestDTO, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return agendaService.createAgenda(agendaRequestDTO, user);
    }

    @PreAuthorize("hasAuthority('UBSADMIN')")
    @PutMapping("/{id}")
    public Object aprovaAgenda(@PathVariable("id") String agendaId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return agendaService.aprovaAgenda(agendaId, user);
    }

    @GetMapping
    public List<AgendaResponseDTO> getAgendas(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return agendaService.getAgendas(user);
    }

    @PatchMapping("/{id}/cancelar")
    @PreAuthorize("hasAuthority('USER')") // Apenas o próprio paciente pode cancelar
    public ResponseEntity<Void> cancelarAgendamento(
            @PathVariable String id,
            Authentication authentication
    ) {
        User paciente = (User) authentication.getPrincipal();
        agendaService.cancelarAgenda(id, paciente);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AgendaResponseDTO> getAgendaPorId(@PathVariable String id) {
        return ResponseEntity.ok(agendaService.getById(id));
    }
}

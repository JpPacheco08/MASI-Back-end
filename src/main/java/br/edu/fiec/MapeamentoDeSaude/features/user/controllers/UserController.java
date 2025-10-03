package br.edu.fiec.MapeamentoDeSaude.features.user.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.user.dto.MyUserDto;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // AQUI VOCÊ VAI ADICIONAR OS ENDPOINTS PARA CADA TIPO DE USUÁRIO
    // Ex: @PostMapping("/admin"), @PostMapping("/ubsadmin"), etc.
    // Por enquanto, vamos criar o endpoint para buscar os dados do usuário logado.

    @GetMapping("/me")
    public MyUserDto getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        // Este método 'getMe' ainda precisa ser implementado no seu UserService
        // return userService.getMe(user);

        // Retorno provisório enquanto o método não existe:
        MyUserDto dto = new MyUserDto();
        dto.setNome(user.getName());
        dto.setEmail(user.getEmail());
        dto.setTipo(user.getAccessLevel().name());
        dto.setPicture(user.getPicture());
        return dto;
    }
}
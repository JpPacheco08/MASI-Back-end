package br.edu.fiec.MapeamentoDeSaude.features.user.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.user.dto.*;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
import br.edu.fiec.MapeamentoDeSaude.utils.ImageUtils;
import jakarta.validation.Valid; // Importar
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // endpoints de registo adicionados
    @PostMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')") // Apenas ADMIN pode criar ADMIN
    public CreatedUserResponseDto registerAdmin(@Valid @RequestBody RegisterAdminDto registerAdminDto) {
        return userService.saveAdmin(registerAdminDto);
    }

    @PostMapping("/ubsadmin")
    @PreAuthorize("hasAuthority('ADMIN')") // Apenas ADMIN pode criar UBSADMIN
    public CreatedUserResponseDto registerUbsAdmin(@Valid @RequestBody RegisterUbsAdminDto registerUbsAdminDto) {
        return userService.saveUbsAdmin(registerUbsAdminDto);
    }

    // Rota pública para pacientes (definida no SecurityConfig)
    @PostMapping("/paciente")
    public CreatedUserResponseDto registerPaciente(@Valid @RequestBody RegisterPacienteDto registerPacienteDto) {
        return userService.savePaciente(registerPacienteDto);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public MyUserDto getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        // Lógica correta: chamar o serviço
        return userService.getMe(user);
    }

    @PutMapping("/photo")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public void insertUserImage(@RequestParam("image") MultipartFile image, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        String imageName = ImageUtils.saveImage(image);
        user.setPicture(imageName);
        userService.save(user);
    }
}
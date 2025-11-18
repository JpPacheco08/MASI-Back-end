package br.edu.fiec.MapeamentoDeSaude.features.user.controllers;

import br.edu.fiec.MapeamentoDeSaude.features.user.dto.*;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.RegisterState; // 1. IMPORTAR RegisterState
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
// import br.edu.fiec.MapeamentoDeSaude.utils.ImageUtils; // 2. REMOVER ESTA LINHA
import br.edu.fiec.MapeamentoDeSaude.shared.service.S3Service; // 3. IMPORTAR S3Service
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException; // 4. IMPORTAR IOException

@RestController
@RequestMapping("/v1/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final S3Service s3Service; // 5. INJETAR O S3Service

    // ... (seus outros endpoints /admin, /ubsadmin, /paciente não mudam) ...
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

    @PostMapping("/paciente")
    public CreatedUserResponseDto registerPaciente(@Valid @RequestBody RegisterPacienteDto registerPacienteDto) {
        return userService.savePaciente(registerPacienteDto);
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public MyUserDto getMe(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return userService.getMe(user);
    }



    // 6. MÉTODO TOTALMENTE MODIFICADO
    @PutMapping("/photo")
    @PreAuthorize("isAuthenticated()") // Qualquer usuário logado
    public void insertUserImage(@RequestParam("image") MultipartFile image, Authentication authentication) throws IOException {
        User user = (User) authentication.getPrincipal();

        // Lógica antiga (local):
        // String imageName = ImageUtils.saveImage(image);

        // Lógica nova (S3):
        String imageName = s3Service.uploadFile(image);

        user.setPicture(imageName);
        user.setState(RegisterState.IMAGE_CREATED); // Atualiza o estado do usuário
        userService.save(user);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/csv")
    public void createUsers(@RequestParam("inputFile") MultipartFile file) throws IOException {
        userService.createUsers(file.getInputStream());
    }
}
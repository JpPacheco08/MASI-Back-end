package br.edu.fiec.MapeamentoDeSaude.features.auth.services.impl;

import br.edu.fiec.MapeamentoDeSaude.features.auth.dto.LoginRequest;
import br.edu.fiec.MapeamentoDeSaude.features.auth.dto.RegisterRequest;
import br.edu.fiec.MapeamentoDeSaude.features.auth.services.AuthService;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.RegisterState; // IMPORTAR
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.UserLevel;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
import br.edu.fiec.MapeamentoDeSaude.utils.PasswordEncryptor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public User register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(PasswordEncryptor.encrypt(request.getPassword()));
        user.setAccessLevel(UserLevel.USER);
        user.setPicture(request.getPicture());

        // ADICIONE ESTA LINHA PARA USAR O ESTADO FALTANTE
        user.setState(RegisterState.USER_CREATED);

        return userService.save(user);
    }

    @Override
    public User login(LoginRequest request) {
        return userService.findByEmail(request.getEmail())
                .filter(user -> PasswordEncryptor.getInstance().matches(request.getPassword(), user.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Email ou senha inválidos."));
    }
}
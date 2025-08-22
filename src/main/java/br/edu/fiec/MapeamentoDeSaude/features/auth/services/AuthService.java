package br.edu.fiec.MapeamentoDeSaude.features.auth.services;

import br.edu.fiec.MapeamentoDeSaude.features.auth.dto.LoginRequest;
import br.edu.fiec.MapeamentoDeSaude.features.auth.dto.RegisterRequest;
import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;

public interface AuthService {
    User register(RegisterRequest request);
    User login(LoginRequest request);
}
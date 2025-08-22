package br.edu.fiec.MapeamentoDeSaude.features.auth.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
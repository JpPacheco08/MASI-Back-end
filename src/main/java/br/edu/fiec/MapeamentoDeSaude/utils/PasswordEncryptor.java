package br.edu.fiec.MapeamentoDeSaude.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordEncryptor {

    // A instância agora não é final e começa como nula
    private static PasswordEncoder passwordEncoder;

    // Este é o método que está faltando!
    // Ele cria a instância apenas na primeira vez que é chamado.
    public static PasswordEncoder getInstance(){
        if(passwordEncoder == null){
            passwordEncoder = new BCryptPasswordEncoder();
        }
        return passwordEncoder;
    }

    private PasswordEncryptor() {
        // Construtor privado para impedir a instanciação
    }

    // O método encrypt agora usa a instância
    public static String encrypt(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("A senha não pode ser nula.");
        }
        return getInstance().encode(rawPassword);
    }

    // O método matches agora usa a instância
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return getInstance().matches(rawPassword, encodedPassword);
    }
}
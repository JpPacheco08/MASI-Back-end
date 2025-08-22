package br.edu.fiec.MapeamentoDeSaude.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


public final class PasswordEncryptor {

    // Instância única do PasswordEncoder, thread-safe.
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private PasswordEncryptor() {
        // Nada a fazer
    }

    public static String encrypt(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("A senha não pode ser nula.");
        }
        return passwordEncoder.encode(rawPassword);
    }


    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
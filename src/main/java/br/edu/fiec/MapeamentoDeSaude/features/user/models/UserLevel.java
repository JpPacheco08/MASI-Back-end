package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import org.springframework.security.core.GrantedAuthority;

public enum UserLevel implements GrantedAuthority {
    USER,
    ADMIN,
    UBSADMIN;

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
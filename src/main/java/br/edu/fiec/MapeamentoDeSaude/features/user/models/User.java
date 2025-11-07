package br.edu.fiec.MapeamentoDeSaude.features.user.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Importar
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class User implements UserDetails {

    // ... (id, email, password, name, accessLevel, picture, fcmToken) ...
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserLevel accessLevel;

    @Column
    private String picture;

    @Column
    private String fcmToken;

    // Este campo é essencial para a lógica do S3
    @Column
    @Enumerated(EnumType.STRING)
    private RegisterState state;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Esta implementação é mais robusta, como a do professor
        return List.of(new SimpleGrantedAuthority(accessLevel.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
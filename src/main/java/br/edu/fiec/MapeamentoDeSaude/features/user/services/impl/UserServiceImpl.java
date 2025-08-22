package br.edu.fiec.MapeamentoDeSaude.features.user.services.impl;


import br.edu.fiec.MapeamentoDeSaude.features.user.models.User;
import br.edu.fiec.MapeamentoDeSaude.features.user.repositories.UserRepository;
import br.edu.fiec.MapeamentoDeSaude.features.user.services.UserService;
import br.edu.fiec.MapeamentoDeSaude.utils.PasswordEncryptor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        //this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User save(User user) {
        // Criptografa a senha antes de salvar
        if (user.getPassword() != null) {
            user.setPassword(PasswordEncryptor.encrypt(user.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, User updatedUser) {
        return userRepository.findById(id).map(user -> {
            user.setName(updatedUser.getName());
            user.setAccessLevel(updatedUser.getAccessLevel());
            user.setPicture(updatedUser.getPicture());

            // Re-criptografa a senha apenas se uma nova for fornecida
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                user.setPassword(PasswordEncryptor.encrypt(updatedUser.getPassword()));
            }
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));
    }

    @Override
    public void deleteById(UUID id) {
        userRepository.deleteById(id);
    }
}
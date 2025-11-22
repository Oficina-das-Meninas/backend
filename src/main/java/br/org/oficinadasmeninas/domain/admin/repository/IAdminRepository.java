package br.org.oficinadasmeninas.domain.admin.repository;

import br.org.oficinadasmeninas.domain.admin.Admin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAdminRepository {

    Admin insert(Admin admin);

    Admin update(Admin admin);

    List<Admin> findAll();

    Optional<Admin> findById(UUID id);

    Optional<Admin> findByEmail(String email);
    
    void updatePassword(UUID id, String encodedPassword);
}

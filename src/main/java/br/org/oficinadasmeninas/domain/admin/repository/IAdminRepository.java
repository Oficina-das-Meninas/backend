package br.org.oficinadasmeninas.domain.admin.repository;

import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.presentation.shared.PageDTO;

public interface IAdminRepository {

    Admin insert(Admin admin);

    Admin update(Admin admin);

    Optional<Admin> findById(UUID id);

    Optional<Admin> findByEmail(String email);
    
    void updatePassword(UUID id, String encodedPassword);
  
    PageDTO<Admin> findByFilter(String searchTerm, int page, int pageSize);
}

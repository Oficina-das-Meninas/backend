package br.org.oficinadasmeninas.domain.admin.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.Admin;

public interface IAdminRepository {
	
	List<Admin> findAll();

	Optional<Admin> findById(UUID id);

	Optional<Admin> findByEmail(String email);

	UUID create(Admin admin);

	void update(Admin admin);

}

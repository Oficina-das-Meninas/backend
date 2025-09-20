package br.org.oficinadasmeninas.domain.admin.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.admin.Admin;

public interface IAdminRepository {
	
	List<Admin> findAllAdmins();

	Optional<Admin> findAdminById(UUID id);

	Optional<Admin> findAdminByEmail(String email);

	UUID createAdmin(Admin admin);

	void updateAdmin(Admin admin);

}

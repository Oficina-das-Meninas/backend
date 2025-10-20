package br.org.oficinadasmeninas.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.User;

public interface IUserRepository {

	List<User> findAll();

	Optional<User> findById(UUID id);

	Optional<User> findByEmail(String email);
	
	boolean existsByEmail(String email);
	
	boolean existsByDocument(String document);

	UUID create(User user);

	void update(User user);
	
}

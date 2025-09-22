package br.org.oficinadasmeninas.domain.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.User;

public interface IUserRepository {

	List<User> findAllUsers();

	Optional<User> findUserById(UUID id);

	Optional<User> findUserByEmail(String email);
	
	boolean existsByEmail(String email);
	
	boolean existsByDocument(String document);

	UUID createUser(User user);

	void updateUser(User user);
	
}

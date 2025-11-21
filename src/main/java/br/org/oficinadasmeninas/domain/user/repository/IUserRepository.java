package br.org.oficinadasmeninas.domain.user.repository;

import br.org.oficinadasmeninas.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserRepository {

    User insert(User user);

    User update(User user);

	List<User> findAll();

	Optional<User> findById(UUID id);

	Optional<User> findByEmail(String email);

	Optional<User> findByDocument(String document);

	boolean existsByEmail(String email);

	boolean existsByDocument(String document);

	void markUserAsVerified(UUID id);
}

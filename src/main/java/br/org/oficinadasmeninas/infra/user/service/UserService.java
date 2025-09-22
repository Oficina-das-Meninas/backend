package br.org.oficinadasmeninas.infra.user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.transparency.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.shared.exception.DocumentAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;

@Service
public class UserService implements IUserService {

	private final IUserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
		super();
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public List<UserDto> getAllUsers() {
		return userRepository.findAllUsers().stream().map(user -> new UserDto(user.getId(), user.getName(),
				user.getEmail(), user.getDocument(), user.getPhone())).toList();
	}

	@Override
	public UserDto getUserById(UUID id) {
		UserDto userDto = new UserDto();

		User user = userRepository.findUserById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setPhone(user.getPhone());
		userDto.setDocument(user.getDocument());

		return userDto;
	}
	
	@Override
	public UserDto getUserByEmail(String email) {
		UserDto userDto = new UserDto();

		User user = userRepository.findUserByEmail(email)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com o email: " + email));

		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setPhone(user.getPhone());
		userDto.setDocument(user.getDocument());

		return userDto;
	}

	@Override
	public UserDto createUser(CreateUserDto user) {
		try {
			User newUser = new User();
			newUser.setName(user.getName());
			newUser.setEmail(user.getEmail());
			newUser.setDocument(user.getDocument());
			newUser.setPhone(user.getPhone());
			newUser.setPassword(passwordEncoder.encode(user.getPassword()));
			
			UUID userId = userRepository.createUser(newUser);
			newUser.setId(userId);
			
			return new UserDto(newUser);
		} catch (DataIntegrityViolationException e) {
			if (userRepository.existsByEmail(user.getEmail())) {
		        throw new EmailAlreadyExistsException();
		    }
		    if (userRepository.existsByDocument(user.getDocument())) {
		        throw new DocumentAlreadyExistsException();
		    }
		    throw e;
		}
	}

	@Override
	public void updateUser(UUID id, UpdateUserDto user) {
        User existingUser = userRepository.findUserById(id)
				.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com id: " + id));

        if (user.getName() != null && !user.getName().isBlank()) {
            existingUser.setName(user.getName());
        }

        if (user.getEmail() != null && !user.getEmail().isBlank() &&
            !user.getEmail().equals(existingUser.getEmail())) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getDocument() != null && !user.getDocument().isBlank()) {
            existingUser.setDocument(user.getDocument());
        }

        if (user.getPhone() != null && !user.getPhone().isBlank()) {
            existingUser.setPhone(user.getPhone());
        }

        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        try {
            userRepository.updateUser(existingUser);
        } catch (DataIntegrityViolationException e) {
        	throw new EmailAlreadyExistsException();
        }

	}

}

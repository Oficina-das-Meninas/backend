package br.org.oficinadasmeninas.infra.user.service;


import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.presentation.exceptions.ConflictException;
import br.org.oficinadasmeninas.presentation.exceptions.InvalidPasswordException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.session.service.SessionService;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder, SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
    }

    @Override
    public UserDto insert(CreateUserDto request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new ConflictException(Messages.EMAIL_ALREADY_EXISTS);

        if (userRepository.existsByDocument(request.getDocument()))
            throw new ConflictException(Messages.DOCUMENT_ALREADY_EXISTS);

        var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setDocument(request.getDocument());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(false);

        userRepository.insert(user);
        return new UserDto(user);
    }

    @Override
    public UUID update(UUID id, UpdateUserDto request) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_ID + id));

        fillUserChanges(request, user);

        userRepository.update(user);
        return user.getId();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository
                .findAll().stream()
                .map(UserDto::new)
                .toList();
    }

    @Override
    public UserDto findByUserId(UUID id) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_ID + id));

        return new UserDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {

        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_EMAIL + email));

        return new UserDto(user);
    }

    @Override
    public UserDto findByDocument(String document) {

        var user = userRepository.findByDocument(document)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_DOCUMENT + document));

        return new UserDto(user);
    }

    @Override
    public void markUserAsVerified(UUID id) {
        userRepository.markUserAsVerified(id);
    }

    @Override
    public void updatePassword(UUID accountId, String encodedPassword) {
        userRepository.updatePassword(accountId, encodedPassword);
    }

    @Override
    public UserDto findByUserSession() {
        String userEmail = sessionService.getSession().getUsername();

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_EMAIL + userEmail));

        return new UserDto(user);
    }

    @Override
    public Void verifyUserPassword(String password) {
        String userEmail = sessionService.getSession().getUsername();

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_EMAIL + userEmail));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidPasswordException(Messages.INVALID_PASSWORD);
        }

        return null;
    }

    private void fillUserChanges(UpdateUserDto request, User user) {

        request.name().ifPresent(user::setName);

        request.phone().ifPresent(user::setPhone);

        request.password().ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));

        request.document().ifPresent(document -> {

            if (!document.equals(user.getDocument())) {
                if (userRepository.findByDocument(document).isPresent())
                    throw new ValidationException(Messages.DOCUMENT_ALREADY_EXISTS);

                user.setDocument(document);
            }
        });

        request.email().ifPresent(email -> {
            if (!email.equals(user.getEmail())) {

                if (userRepository.findByEmail(email).isPresent())
                    throw new ValidationException(Messages.EMAIL_ALREADY_EXISTS);

                user.setEmail(email);
            }
        });
    }
}

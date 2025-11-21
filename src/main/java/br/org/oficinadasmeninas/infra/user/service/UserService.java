package br.org.oficinadasmeninas.infra.user.service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.email.service.EmailService;
import br.org.oficinadasmeninas.infra.shared.exception.DocumentAlreadyExistsException;
import br.org.oficinadasmeninas.infra.shared.exception.EmailAlreadyExistsException;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;

    public UserService(IUserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.jwtService = jwtService;
    }

    @Override
    public UserDto insert(CreateUserDto request) {
        try {
            var user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setDocument(request.getDocument());
            user.setPhone(request.getPhone());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setInactive(true);

            userRepository.insert(user);
            
            sendConfirmAccountEmail(user);
            
            return new UserDto(user);

        } catch (DataIntegrityViolationException e) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExistsException();
            }
            if (userRepository.existsByDocument(request.getDocument())) {
                throw new DocumentAlreadyExistsException();
            }
            throw e;
        }
    }

    @Override
    public UUID update(UUID id, UpdateUserDto user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_ID + id));

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
            userRepository.update(existingUser);
            return existingUser.getId();
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyExistsException();
        }
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
    
    private void sendConfirmAccountEmail(User user) {
    	String to = user.getEmail();
        String subject = "Confirmação de conta";
        String greeting = "Olá, " + user.getName();
        
        String verifyEmailToken = jwtService.generateVerifyEmailToken(
        		new UserDetailsCustom(
        				null, 
        				user.getEmail(), 
        				user.getPassword(), 
        				user.getName(), 
        				false
        			)
        		);
        
        String magicLink = "http://localhost:4200/verificar-email?token="+verifyEmailToken;
        String href = String.format("<a href='%s'>Verificar e-mail</a>", magicLink);
        
        String contentHtml = "<p>Clique no link abaixo para verificar sua conta:</p>" +
        		href;
        
        emailService.sendWithDefaultTemplate(to, subject, greeting, contentHtml);
    }

}

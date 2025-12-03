package br.org.oficinadasmeninas.infra.account.service;

import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;

import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.email.service.EmailService;
import br.org.oficinadasmeninas.infra.user.service.UserService;

@Service
public class EmailVerificationService {

    private final JwtService jwtService;
    private final UserService userService;
    private final EmailService emailService;

    public EmailVerificationService(JwtService jwtService, UserService userService, EmailService emailService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.emailService = emailService;
    }

    public Void verifyUserEmail(String token) {
        final var userId = jwtService.extractUserId(token);

        if (userId == null) {
            throw new UnauthorizedException(Messages.INVALID_EMAIL_TOKEN);
        }

        var userDto = userService.findByUserId(userId);

        if (userDto.isActive())
            return null;

        var userDetails = new UserDetailsCustom(userDto.getId(), userDto.getEmail(), null, userDto.getName(), false);

        var isTokenValid = jwtService.isTokenValidForPurpose(
                token,
                userDetails,
                JwtService.PurposeTokenEnum.VERIFY_EMAIL
        );

        if (!isTokenValid)
            throw new UnauthorizedException(Messages.INVALID_EMAIL_TOKEN);

        userService.activateUser(userDto.getId(), userDto.getEmail(), userDto.getDocument());
        return null;
    }
    
    public Void sendVerifyAccountEmail(String email) {
    	UserDto userDto = userService.findByEmail(email);
    	
    	if(userDto.isActive()) {
    		throw new ValidationException(Messages.EMAIL_ALREADY_VERIFIED);
    	}
    	
    	emailService.sendConfirmUserAccountEmail(userDto.getEmail(), userDto.getName(), userDto.getId().toString());
    	return null;
    }
}

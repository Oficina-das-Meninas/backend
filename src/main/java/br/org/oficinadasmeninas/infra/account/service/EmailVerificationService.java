package br.org.oficinadasmeninas.infra.account.service;

import br.org.oficinadasmeninas.infra.admin.service.AdminService;
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
    private final AdminService adminService;
    private final EmailService emailService;

    public EmailVerificationService(
        JwtService jwtService,
        UserService userService,
        AdminService adminService,
        EmailService emailService
    ) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.adminService = adminService;
        this.emailService = emailService;
    }

    public Void verifyUserEmail(String token) {
        final var username = jwtService.extractUsername(token);

        if (username == null) {
            throw new UnauthorizedException(Messages.INVALID_EMAIL_TOKEN);
        }

        UserDetailsCustom userDetails = null;

        try {
            var adminDto = adminService.findByEmail(username);
            userDetails = new UserDetailsCustom(adminDto.getId(), adminDto.getEmail(), null, adminDto.getName(), true);
        } catch (Exception ignored) {}

        if (userDetails == null) {
            var userDto = userService.findByEmail(username);
            if (userDto.isActive())
                return null;
            userDetails = new UserDetailsCustom(userDto.getId(), userDto.getEmail(), null, userDto.getName(), false);
        }

        var isResetTokenValid = jwtService.isTokenValidForPurpose(
                token,
                userDetails,
                JwtService.PurposeTokenEnum.RESET_PASSWORD
        );

        var isVerifyTokenValid = jwtService.isTokenValidForPurpose(
                token,
                userDetails,
                JwtService.PurposeTokenEnum.VERIFY_EMAIL
        );

        if (!isResetTokenValid && !isVerifyTokenValid)
            throw new UnauthorizedException(Messages.INVALID_EMAIL_TOKEN);

        if (Boolean.FALSE.equals(userDetails.getAdmin())) {
            userService.markUserAsVerified(userDetails.getId());
        }

        return null;
    }
    
    public Void sendVerifyAccountEmail(String email) {
    	UserDto userDto = userService.findByEmail(email);
    	
    	if(userDto.isActive()) {
    		throw new ValidationException(Messages.EMAIL_ALREADY_VERIFIED);
    	}
    	
    	emailService.sendConfirmUserAccountEmail(userDto.getEmail(), userDto.getName());
    	return null;
    }
}

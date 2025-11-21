package br.org.oficinadasmeninas.infra.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.shared.exception.TokenValidationException;
import br.org.oficinadasmeninas.infra.user.service.UserService;

@Service
public class EmailVerificationService {

    private final JwtService jwtService;
    private final UserService userService;

    public EmailVerificationService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public String verifyUserEmail(String token) {
        final String username = jwtService.extractUsername(token);

        if (username == null) {
            throw new TokenValidationException(Messages.INVALID_EMAIL_TOKEN);
        }

        UserDto userDto = userService.findByEmail(username);
        
        UserDetails userDetails = new UserDetailsCustom(userDto.getId(), userDto.getEmail(), null, userDto.getName(), false);
        boolean verified = jwtService.isTokenValidForPurpose(
            token, 
            userDetails, 
            JwtService.PurposeTokenEnum.VERIFY_EMAIL
        );
        
        if (verified && userDto != null && userDto.isInactive()) {
        	userService.markUserAsVerified(userDto.getId());
            return "E-mail verificado com sucesso"; 
        }
        
        throw new TokenValidationException(Messages.INVALID_EMAIL_TOKEN);
    }
}

package br.org.oficinadasmeninas.infra.account.service;

import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.user.service.UserService;

@Service
public class EmailVerificationService {

    private final JwtService jwtService;
    private final UserService userService;

    public EmailVerificationService(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public Void verifyUserEmail(String token) {
        final var username = jwtService.extractUsername(token);

        if (username == null) {
            throw new UnauthorizedException(Messages.INVALID_EMAIL_TOKEN);
        }

        var userDto = userService.findByEmail(username);

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

        userService.markUserAsVerified(userDto.getId());
        return null;
    }
}

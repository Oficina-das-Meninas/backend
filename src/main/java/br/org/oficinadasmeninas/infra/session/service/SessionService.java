package br.org.oficinadasmeninas.infra.session.service;

import br.org.oficinadasmeninas.presentation.shared.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.infra.session.dto.SessionResponseDto;
import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;

@Service
public class SessionService {

    private static final String ACCESS_TOKEN = "access_token";
	
    public SessionResponseDto getSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null 
        	|| !authentication.isAuthenticated() 
        	|| "anonymousUser".equals(authentication.getPrincipal())
        ) {
            throw new UnauthorizedException("Usuário não autorizado.");
        }

        String username = authentication.getName();
        return new SessionResponseDto(username);
    }

    public Boolean hasSession(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, ACCESS_TOKEN);
        return token != null;
    }

}

package br.org.oficinadasmeninas.infra.session.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.infra.session.dto.SessionResponseDto;
import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;

@Service
public class SessionService {
	
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

}

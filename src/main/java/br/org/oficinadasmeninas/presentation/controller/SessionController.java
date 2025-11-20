package br.org.oficinadasmeninas.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.infra.session.service.SessionService;

@RestController
@RequestMapping("/api/sessions")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class SessionController extends BaseController {
	
	private final SessionService sessionService;
	
	public SessionController(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	@GetMapping
	public ResponseEntity<?> getSession() {	
		return handle(
			sessionService::getSession,
			Messages.SESSION_USER_SUCCESSFULLY
		);
	}

}

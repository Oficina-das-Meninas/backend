package br.org.oficinadasmeninas.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.infra.account.dto.ResetPasswordDto;
import br.org.oficinadasmeninas.infra.account.service.EmailVerificationService;
import br.org.oficinadasmeninas.infra.account.service.ResetPasswordService;
import br.org.oficinadasmeninas.infra.auth.dto.LoginUserDto;
import br.org.oficinadasmeninas.infra.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController extends BaseController {

	private final AuthService authService;
	private final EmailVerificationService emailVerificationService;
	private final ResetPasswordService resetPasswordService;

	public AuthController(AuthService authService, EmailVerificationService emailVerificationService, ResetPasswordService resetPasswordService) {
		this.authService = authService;
		this.emailVerificationService = emailVerificationService;
		this.resetPasswordService = resetPasswordService;
	}

	@PostMapping("/signup")
	public ResponseEntity<?> createUserAccount(
		@Valid @RequestBody CreateUserDto request
	) {
		return handle(
			() -> authService.createUserAccount(request),
			Messages.USER_CREATED_SUCCESSFULLY,
			HttpStatus.CREATED
		);
	}

	@PostMapping("/login")
	public ResponseEntity<?> authenticate(
		@RequestBody LoginUserDto loginUserDto, HttpServletResponse response
	) {
		return handle(
			() -> authService.login(loginUserDto, response),
			Messages.AUTH_LOGIN_SUCCESSFULLY
		);
	}
	
	@GetMapping("/logout")
	public ResponseEntity<?> logout(
		HttpServletRequest request, HttpServletResponse response
	) {	
		return handle(
			() -> authService.logout(request, response),
			Messages.AUTH_LOGOUT_SUCCESSFULLY
		);
	}
	
	@GetMapping("/verify-email")
	public ResponseEntity<?> verifyUserEmail(
		@RequestParam String token
	) {	
		return handle(
			() -> emailVerificationService.verifyUserEmail(token),
			Messages.EMAIL_VERIFIED_SUCCESSFULLY
		);
	}
	
	@GetMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(
		@RequestParam String email
	) {	
		return handle(
			() -> resetPasswordService.sendResetPasswordEmail(email),
			Messages.EMAIL_SENDED_SUCCESSFULLY
		);
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> resetPassword(
		@RequestParam String token, @RequestBody ResetPasswordDto resetPasswordDto
	) {	
		return handle(
			() -> resetPasswordService.resetPassword(token, resetPasswordDto),
			Messages.PASSWORD_CHANGED_SUCCESSFULLY
		);
	}

}

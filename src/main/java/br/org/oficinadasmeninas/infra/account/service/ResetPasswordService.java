package br.org.oficinadasmeninas.infra.account.service;

import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.infra.account.dto.ResetPasswordDto;
import br.org.oficinadasmeninas.infra.admin.service.AdminService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.email.service.EmailService;
import br.org.oficinadasmeninas.infra.shared.exception.TokenValidationException;
import br.org.oficinadasmeninas.infra.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@Service
public class ResetPasswordService {

	private final JwtService jwtService;
	private final UserService userService;
	private final AdminService adminService;
	private final PasswordEncoder passwordEncoder;
	private final EmailService emailService;

	public ResetPasswordService(JwtService jwtService, UserService userService, AdminService adminService, PasswordEncoder passwordEncoder, EmailService emailService) {
		super();
		this.jwtService = jwtService;
		this.userService = userService;
		this.adminService = adminService;
		this.passwordEncoder = passwordEncoder;
		this.emailService = emailService;
	}
	
	public Void resetPassword(String token, ResetPasswordDto resetPasswordDto, HttpServletRequest request) {
		final UUID userId = jwtService.extractUserId(token);
		final String username = jwtService.extractUsername(token);
		boolean isAdminOrigin = isAdminOrigin(request);

		if (userId == null || username == null) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}

		boolean isAdmin = false;
		try {
			AdminDto adminDto = adminService.findByEmail(username);
			if (adminDto.getId().equals(userId)) {
				isAdmin = true;
				resetAdminPassword(token, resetPasswordDto);
				return null;
			}
		} catch (Exception e) {}

		if (isAdminOrigin && !isAdmin) {
			throw new TokenValidationException("Token inválido para origem administrativa");
		}

		resetUserPassword(token, resetPasswordDto);
		return null;
		
	}
	
	public Void validateResetToken(String token, HttpServletRequest request) {
		final UUID userId = jwtService.extractUserId(token);
		final String username = jwtService.extractUsername(token);
		boolean isAdminOrigin = isAdminOrigin(request);

		if (userId == null || username == null) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}

		UserDetails userDetails = null;

		if (isAdminOrigin) {
			AdminDto adminDto = adminService.findByEmail(username);
			if (!adminDto.getId().equals(userId)) {
				throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
			}
			userDetails = new UserDetailsCustom(adminDto.getId(), adminDto.getEmail(), null, adminDto.getName(), true);
		} else {
			try {
				AdminDto adminDto = adminService.findByEmail(username);
				if (adminDto.getId().equals(userId)) {
					userDetails = new UserDetailsCustom(adminDto.getId(), adminDto.getEmail(), null, adminDto.getName(), true);
				}
			} catch (Exception e) {
			}

			if (userDetails == null) {
				UserDto userDto = userService.findByEmail(username);
				if (!userDto.getId().equals(userId)) {
					throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
				}
				userDetails = new UserDetailsCustom(userDto.getId(), userDto.getEmail(), null, userDto.getName(), false);
			}
		}

		boolean verified = jwtService.isTokenValidForPurpose(token, userDetails,
				JwtService.PurposeTokenEnum.RESET_PASSWORD);

		if (!verified) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}

		return null;
	}

	public Void sendResetPasswordEmail(String email, HttpServletRequest request) {
	    boolean isAdminOrigin = isAdminOrigin(request);

	    if (isAdminOrigin) {
	        AdminDto admin = adminService.findByEmail(email);
	        emailService.sendResetPasswordEmail(admin.getEmail(), admin.getName(), admin.getId(), true);
	        return null;
	    }

	    try {
	        AdminDto admin = adminService.findByEmail(email);
	        emailService.sendResetPasswordEmail(admin.getEmail(), admin.getName(), admin.getId(), true);
	        return null;
	    } catch (Exception e) {
	    }

	    UserDto user = userService.findByEmail(email);

        if(!user.isActive()) {
        	throw new ValidationException(Messages.USER_NOT_VERIFIED);
        }

        emailService.sendResetPasswordEmail(user.getEmail(), user.getName(), user.getId(), false);

	    return null;
	}

	private boolean isAdminOrigin(HttpServletRequest request) {
		String origin = request.getHeader("Origin");
		if (origin == null || origin.isEmpty()) {
			return false;
		}

		return origin.contains("admin.oficinadasmeninas.org.br") ||
		       origin.contains("admin-dev.oficinadasmeninas.org.br");
	}

	private void resetUserPassword(String token, ResetPasswordDto resetPasswordDto) {
		final String username = jwtService.extractUsername(token);

		if (username == null) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}
		
		UserDto userDto = userService.findByEmail(username);
		
		UserDetails userDetails = new UserDetailsCustom(userDto.getId(), userDto.getEmail(), null, userDto.getName(), false);
		boolean verified = jwtService.isTokenValidForPurpose(token, userDetails,
				JwtService.PurposeTokenEnum.RESET_PASSWORD);

		if (!verified) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}

		final String encodedPassword = passwordEncoder.encode(resetPasswordDto.newPassword());
		
		userService.updatePassword(userDto.getAccountId(), encodedPassword);
	}
	
	private void resetAdminPassword(String token, ResetPasswordDto resetPasswordDto) {
		final String username = jwtService.extractUsername(token);

		if (username == null) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}
		
		AdminDto adminDto = adminService.findByEmail(username);
		
		UserDetails userDetails = new UserDetailsCustom(adminDto.getId(), adminDto.getEmail(), null, adminDto.getName(), true);
		boolean verified = jwtService.isTokenValidForPurpose(token, userDetails,
				JwtService.PurposeTokenEnum.RESET_PASSWORD);

		if (!verified) {
			throw new TokenValidationException(Messages.INVALID_PASSWORD_TOKEN);
		}

		final String encodedPassword = passwordEncoder.encode(resetPasswordDto.newPassword());

		adminService.updatePassword(adminDto.getId(), encodedPassword);
	}

}

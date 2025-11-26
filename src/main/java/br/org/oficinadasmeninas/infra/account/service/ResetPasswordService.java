package br.org.oficinadasmeninas.infra.account.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.infra.account.dto.ResetPasswordDto;
import br.org.oficinadasmeninas.infra.admin.service.AdminService;
import br.org.oficinadasmeninas.infra.auth.AuthoritiesEnum;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.infra.email.service.EmailService;
import br.org.oficinadasmeninas.infra.shared.exception.TokenValidationException;
import br.org.oficinadasmeninas.infra.shared.exception.UserNotVerifiedException;
import br.org.oficinadasmeninas.infra.user.service.UserService;

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
	
	public Void resetPassword(String token, ResetPasswordDto resetPasswordDto) {
		final String role = jwtService.extractRole(token);
		
		if(role.equals(AuthoritiesEnum.ROLE_ADMIN.name())) {	
			resetAdminPassword(token, resetPasswordDto);
			return null;
		}
		
		resetUserPassword(token, resetPasswordDto);
		return null;
		
	}
	
	public Void sendResetPasswordEmail(String email) {
	    boolean emailSent = false;

	    try {
	        AdminDto admin = adminService.findByEmail(email);
	        emailService.sendResetPasswordEmail(admin.getEmail(), admin.getName(), true);
	        emailSent = true;
	        
	    } catch (Exception e) {}

	    if (!emailSent) {
	    	UserDto user = userService.findByEmail(email);
            
            if(!user.isActive()) {
            	throw new UserNotVerifiedException(Messages.USER_NOT_VERIFIED);
            }
            
            emailService.sendResetPasswordEmail(user.getEmail(), user.getName(), false);
            emailSent = true;
	    }
	    
	    return null;
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

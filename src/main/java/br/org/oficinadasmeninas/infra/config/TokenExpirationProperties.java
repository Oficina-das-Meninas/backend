package br.org.oficinadasmeninas.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenExpirationProperties {

	@Value("${security.jwt.expiration.reset-password}")
	private long resetPassword;
	
	@Value("${security.jwt.expiration.verify-email}")
	private long verifyEmail;
	
	@Value("${security.jwt.expiration.user-session}")
	private long userSession;
	
	@Value("${security.jwt.expiration.admin-session}")
	private long adminSession;

	public long getResetPassword() {
		return resetPassword;
	}

	public long getVerifyEmail() {
		return verifyEmail;
	}

	public long getUserSession() {
		return userSession;
	}

	public long getAdminSession() {
		return adminSession;
	}

}

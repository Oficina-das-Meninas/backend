package br.org.oficinadasmeninas.infra.auth.dto;

public class LoginResponseDto {

	private String token;
	private UserResponseDto user;
	private Long expiresIn;

	public String getToken() {
		return token;
	}

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserResponseDto getUser() {
		return user;
	}

	public void setUser(UserResponseDto user) {
		this.user = user;
	}
}

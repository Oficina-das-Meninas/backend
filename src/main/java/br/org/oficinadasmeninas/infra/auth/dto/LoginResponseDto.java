package br.org.oficinadasmeninas.infra.auth.dto;

public class LoginResponseDto {

	private UserResponseDto user;
	private Long expiresIn;

	public Long getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public UserResponseDto getUser() {
		return user;
	}

	public void setUser(UserResponseDto user) {
		this.user = user;
	}
}

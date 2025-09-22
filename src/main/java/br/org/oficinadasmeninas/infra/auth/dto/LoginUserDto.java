package br.org.oficinadasmeninas.infra.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginUserDto {

	@NotBlank(message = "O email não pode estar vazio")
	private String email;

	@NotBlank(message = "A senha não pode estar vazia")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

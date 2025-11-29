package br.org.oficinadasmeninas.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserDto {

	@NotBlank(message = "O nome é obrigatório")
	@Size(max = 255, message = "O nome deve ter no máximo 255 caracteres")
	private String name;

	@NotBlank(message = "O email é obrigatório")
	@Email(message = "Email inválido")
	@Size(max = 255, message = "O email deve ter no máximo 255 caracteres")
	private String email;

	@NotBlank(message = "O documento é obrigatório")
	private String document;

	@NotBlank(message = "A senha é obrigatória")
	private String password;

	@NotBlank(message = "O celular é obrigatório")
	@Size(max = 20, message = "O celular deve ter no máximo 20 caracteres")
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}

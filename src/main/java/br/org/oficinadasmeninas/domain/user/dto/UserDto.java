package br.org.oficinadasmeninas.domain.user.dto;

import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.User;

public class UserDto {

	private UUID id;
	private String name;
	private String email;
	private String document;
	private String phone;
	private UUID accountId;

	public UserDto() {
		super();
	}

	public UserDto(User user) {
		super();
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.document = user.getDocument();
		this.phone = user.getPhone();
		this.accountId = user.getAccountId();
	}

	public UserDto(UUID id, String name, String email, String document, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.document = document;
		this.phone = phone;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}
}
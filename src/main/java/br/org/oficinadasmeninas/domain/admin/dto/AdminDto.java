package br.org.oficinadasmeninas.domain.admin.dto;

import java.util.UUID;

public class AdminDto {

	private UUID id;
	private String name;
	private String email;
	private UUID accountId;

	public AdminDto() {
	}

	public AdminDto(UUID id, String name, String email, UUID accountId) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.accountId = accountId;
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

	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

}

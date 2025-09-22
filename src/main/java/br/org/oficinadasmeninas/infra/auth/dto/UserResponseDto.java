package br.org.oficinadasmeninas.infra.auth.dto;

import java.util.UUID;

public class UserResponseDto {
	private UUID id;
	private String name;
	private Boolean isAdmin;

	public UserResponseDto(UUID id, String name, Boolean isAdmin) {
		super();
		this.id = id;
		this.name = name;
		this.isAdmin = isAdmin;
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

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}

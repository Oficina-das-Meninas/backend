package br.org.oficinadasmeninas.infra.session.dto;

public class SessionResponseDto {
	
	private String username;

	public SessionResponseDto(String username) {
		this.username = username;
	}
	
	public SessionResponseDto() {}

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }

}

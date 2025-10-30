package br.org.oficinadasmeninas.infra.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserDetailsCustom implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private final UUID id;
	private final String email;
	private final String password;
	private final String name;
	private final Boolean isAdmin;

	public UserDetailsCustom(UUID id, String email, String password, String name, Boolean isAdmin) {
		super();
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.isAdmin = isAdmin;
	}

	public UUID getId() {
		return id;
	}

	public Boolean getAdmin() {
		return isAdmin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of();
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

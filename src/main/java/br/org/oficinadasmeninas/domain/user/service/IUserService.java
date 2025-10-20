package br.org.oficinadasmeninas.domain.user.service;

import java.util.List;
import java.util.UUID;

import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;

public interface IUserService {

	public List<UserDto> getAllUsers();
	
	public UserDto getUserById(UUID id);
	
	public UserDto getUserByEmail(String email);

    public UserDto getUserByDocument(String document);
	
	public UserDto createUser(CreateUserDto user);
	
	public void updateUser(UUID id, UpdateUserDto user);
	
}

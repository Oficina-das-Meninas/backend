package br.org.oficinadasmeninas.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UpdateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final IUserService userService;

	public UserController(IUserService userService) {
		super();
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto request) {
		UUID id = userService.createUser(request);

		UserDto userDto = new UserDto();
		userDto.setId(id);
		userDto.setEmail(request.getEmail());
		userDto.setName(request.getName());
		userDto.setDocument(request.getDocument());
		userDto.setPhone(request.getPhone());

		return ResponseEntity
				.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri())
				.body(userDto);
	}
	
	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		List<UserDto> dto = userService.getAllUsers();

		return ResponseEntity.ok(dto);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
		UserDto dto = userService.getUserById(id);

		return ResponseEntity.ok(dto);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Void> updateUser(@PathVariable UUID id, @Valid @RequestBody UpdateUserDto request) {
		userService.updateUser(id, request);

		return ResponseEntity.noContent().build();
	}


}

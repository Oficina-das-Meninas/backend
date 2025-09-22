package br.org.oficinadasmeninas.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.dto.LoginResponseDto;
import br.org.oficinadasmeninas.infra.auth.dto.LoginUserDto;
import br.org.oficinadasmeninas.infra.auth.dto.UserResponseDto;
import br.org.oficinadasmeninas.infra.auth.service.AuthService;
import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtService jwtService;

	public AuthController(AuthService authService, JwtService jwtService) {
		super();
		this.authService = authService;
		this.jwtService = jwtService;
	}

	@PostMapping("/signup")
	public ResponseEntity<UserDto> createUserAccount(@Valid @RequestBody CreateUserDto request) {
		UserDto userDto = authService.createUserAccount(request);

		return ResponseEntity.created(
				ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDto.getId()).toUri())
				.body(userDto);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
		UserDetailsCustom authenticatedUser = authService.authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);

		LoginResponseDto loginResponse = new LoginResponseDto();
		UserResponseDto userResponseDTO = new UserResponseDto(authenticatedUser.getId(), authenticatedUser.getName(),
				authenticatedUser.getAdmin());

		loginResponse.setUser(userResponseDTO);
		loginResponse.setToken(jwtToken);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());

		return ResponseEntity.ok(loginResponse);
	}

}

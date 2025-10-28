package br.org.oficinadasmeninas.infra.auth.service;

import br.org.oficinadasmeninas.domain.shared.exception.EntityNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.dto.LoginUserDto;

@Service
public class AuthService {

	private final IAdminService adminService;
	private final IUserService userService;
	private final AuthenticationManager authenticationManager;

	private final boolean IS_ADMIN = true;

	public AuthService(IAdminService adminService, IUserService userService,
			AuthenticationManager authenticationManager) {
		super();
		this.adminService = adminService;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
	}

	public UserDto createUserAccount(CreateUserDto user) {
		UserDto newUser = userService.createUser(user);
		return newUser;
	}

	public UserDetailsCustom authenticate(LoginUserDto loginUserDTO) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));

		UserDto user = userService.getUserByEmail(loginUserDTO.getEmail());

		if (user != null) {
			return createUserDetailsCustom(user, loginUserDTO.getPassword());
		}

		AdminDto admin = adminService.findByEmail(loginUserDTO.getEmail());

		if (admin != null) {
			return createUserDetailsCustom(admin, loginUserDTO.getPassword());
		}

		throw new EntityNotFoundException("Usuário não encontrado");
	}

	private UserDetailsCustom createUserDetailsCustom(UserDto user, String password) {
		return new UserDetailsCustom(user.getId(), user.getEmail(), password, user.getName(), !IS_ADMIN);
	}

	private UserDetailsCustom createUserDetailsCustom(AdminDto admin, String password) {
		return new UserDetailsCustom(admin.getId(), admin.getEmail(), password, admin.getName(), IS_ADMIN);
	}
	
}

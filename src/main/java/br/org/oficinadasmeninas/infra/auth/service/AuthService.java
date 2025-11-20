package br.org.oficinadasmeninas.infra.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.dto.LoginResponseDto;
import br.org.oficinadasmeninas.infra.auth.dto.LoginUserDto;
import br.org.oficinadasmeninas.infra.auth.dto.UserResponseDto;
import br.org.oficinadasmeninas.presentation.shared.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {
	
	private static final boolean IS_ADMIN = true;
	private static final String ACCESS_TOKEN = "access_token";

	private final IAdminService adminService;
	private final IUserService userService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public AuthService(
		IAdminService adminService, 
		IUserService userService,
		AuthenticationManager authenticationManager,
		JwtService jwtService
	) {
		this.adminService = adminService;
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
	}
	
	public LoginResponseDto login(LoginUserDto loginUserDto, HttpServletResponse response) {
		UserDetailsCustom authenticatedUser = authenticate(loginUserDto);

		String jwtToken = jwtService.generateToken(authenticatedUser);
		long expirationTime = jwtService.getExpirationTime();
	
		CookieUtils.addCookie(response, ACCESS_TOKEN, jwtToken, expirationTime);
		
		UserResponseDto userResponse = new UserResponseDto();
		userResponse.setId(authenticatedUser.getId());
		userResponse.setName(authenticatedUser.getName());
		userResponse.setIsAdmin(authenticatedUser.getAdmin());

		LoginResponseDto loginResponse = new LoginResponseDto();
		loginResponse.setUser(userResponse);
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		
		return loginResponse;
	}
	
	public Void logout(HttpServletRequest request, HttpServletResponse response) {
	    CookieUtils.deleteCookie(request, response, ACCESS_TOKEN);
	    SecurityContextHolder.clearContext();
	    
		return null;
	}

	public UserDto createUserAccount(CreateUserDto user) {
		return userService.insert(user);
	}

	private UserDetailsCustom authenticate(LoginUserDto loginUserDTO) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));

		try {
			AdminDto admin = adminService.findByEmail(loginUserDTO.getEmail());

			if (admin != null) {
				return createUserDetailsCustom(admin, loginUserDTO.getPassword());
			}
		}catch(Exception e) {}

		UserDto user = userService.findByEmail(loginUserDTO.getEmail());
		
		return createUserDetailsCustom(user, loginUserDTO.getPassword());
	}

	private UserDetailsCustom createUserDetailsCustom(UserDto user, String password) {
		return new UserDetailsCustom(user.getId(), user.getEmail(), password, user.getName(), !IS_ADMIN);
	}

	private UserDetailsCustom createUserDetailsCustom(AdminDto admin, String password) {
		return new UserDetailsCustom(admin.getId(), admin.getEmail(), password, admin.getName(), IS_ADMIN);
	}
	
}

package br.org.oficinadasmeninas.infra.auth.service;

import br.org.oficinadasmeninas.presentation.exceptions.UnauthorizedException;
import br.org.oficinadasmeninas.presentation.exceptions.ValidationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.oficinadasmeninas.domain.admin.dto.AdminDto;
import br.org.oficinadasmeninas.domain.admin.service.IAdminService;
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.dto.CreateUserDto;
import br.org.oficinadasmeninas.domain.user.dto.UserDto;
import br.org.oficinadasmeninas.domain.user.service.IUserService;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.infra.auth.dto.LoginResponseDto;
import br.org.oficinadasmeninas.infra.auth.dto.LoginUserDto;
import br.org.oficinadasmeninas.infra.auth.dto.UserResponseDto;
import br.org.oficinadasmeninas.infra.email.service.EmailService;
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
    private final EmailService emailService;

    public AuthService(
            IAdminService adminService,
            IUserService userService,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            EmailService emailService
    ) {
        this.adminService = adminService;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
    }

    public LoginResponseDto login(LoginUserDto loginUserDto, HttpServletResponse response) {
        UserDetailsCustom authenticatedUser = authenticate(loginUserDto);

        String jwtToken = jwtService.generateUserSessionToken(authenticatedUser);
        long expirationTime = jwtService.getTokenExpirationProperties().getUserSession();

        if (authenticatedUser.getAdmin()) {
            jwtToken = jwtService.generateAdminSessionToken(authenticatedUser);
            expirationTime = jwtService.getTokenExpirationProperties().getAdminSession();
        }

        CookieUtils.addCookie(response, ACCESS_TOKEN, jwtToken, expirationTime);

        UserResponseDto userResponse = new UserResponseDto();
        userResponse.setId(authenticatedUser.getId());
        userResponse.setName(authenticatedUser.getName());
        userResponse.setIsAdmin(authenticatedUser.getAdmin());

        LoginResponseDto loginResponse = new LoginResponseDto();
        loginResponse.setUser(userResponse);
        loginResponse.setExpiresIn(expirationTime);

        return loginResponse;
    }

    public Void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, ACCESS_TOKEN);
        SecurityContextHolder.clearContext();

        return null;
    }

    @Transactional
    public UserDto createUserAccount(CreateUserDto user) {
        UserDto userDto = userService.insert(user);

        emailService.sendConfirmUserAccountEmail(userDto.getEmail(), userDto.getName());

        return userDto;
    }

    private UserDetailsCustom authenticate(LoginUserDto loginUserDTO) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException(Messages.INVALID_EMAIL_OR_PASSWORD);
        }

        try {
            var admin = adminService.findByEmail(loginUserDTO.getEmail());

            if (admin != null)
                return createUserDetailsCustom(admin, loginUserDTO.getPassword());

        } catch (Exception e) {/* findByEmail pode gerar NotFoundException caso não existe admin */}

        var user = userService.findByEmail(loginUserDTO.getEmail());

        if (!user.isActive())
            throw new ValidationException(Messages.EMAIL_NOT_VERIFIED);

        return createUserDetailsCustom(user, loginUserDTO.getPassword());
    }

    private UserDetailsCustom createUserDetailsCustom(UserDto user, String password) {
        return new UserDetailsCustom(user.getId(), user.getEmail(), password, user.getName(), !IS_ADMIN);
    }

    private UserDetailsCustom createUserDetailsCustom(AdminDto admin, String password) {
        return new UserDetailsCustom(admin.getId(), admin.getEmail(), password, admin.getName(), IS_ADMIN);
    }

}

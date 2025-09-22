package br.org.oficinadasmeninas.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.org.oficinadasmeninas.domain.admin.Admin;
import br.org.oficinadasmeninas.domain.admin.repository.IAdminRepository;
import br.org.oficinadasmeninas.domain.transparency.exception.EntityNotFoundException;
import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;

@Configuration
public class ApplicationConfiguration {

	private final IUserRepository userRepository;
	private final IAdminRepository adminRepository;

	private final boolean IS_ADMIN = true;

	public ApplicationConfiguration(IUserRepository userRepository, IAdminRepository adminRepository) {
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> {
			User user = userRepository.findUserByEmail(username)
					.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
			if (user != null) {
				return createUserDetailsCustom(user);
			}

			Admin admin = adminRepository.findAdminByEmail(username)
					.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
			if (admin != null) {
				return createUserDetailsCustom(admin);
			}

			throw new EntityNotFoundException("Erro ao tentar efetuar autenticação");

		};
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	private UserDetailsCustom createUserDetailsCustom(User user) {
		return new UserDetailsCustom(user.getId(), user.getEmail(), user.getPassword(), user.getName(), !IS_ADMIN);
	}

	private UserDetailsCustom createUserDetailsCustom(Admin admin) {
		return new UserDetailsCustom(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getName(), IS_ADMIN);
	}
}
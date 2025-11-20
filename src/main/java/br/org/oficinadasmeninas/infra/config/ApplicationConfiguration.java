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
import br.org.oficinadasmeninas.domain.resources.Messages;
import br.org.oficinadasmeninas.domain.user.User;
import br.org.oficinadasmeninas.domain.user.repository.IUserRepository;
import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;
import br.org.oficinadasmeninas.presentation.exceptions.NotFoundException;

@Configuration
public class ApplicationConfiguration {

    private final IUserRepository userRepository;
    private final IAdminRepository adminRepository;

    private static final boolean IS_ADMIN = true;

    public ApplicationConfiguration(IUserRepository userRepository, IAdminRepository adminRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> {
        	var admin = adminRepository.findByEmail(username);
        	
        	if (admin.isPresent()) 
        		return createUserDetailsCustom(admin.get()); 
        	
        	
        	var user = userRepository.findByEmail(username)
        			.orElseThrow(() -> new NotFoundException(Messages.USER_NOT_FOUND_BY_EMAIL + username));
        	
        	return createUserDetailsCustom(user);
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
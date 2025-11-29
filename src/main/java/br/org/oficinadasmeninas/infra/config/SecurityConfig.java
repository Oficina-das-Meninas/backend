package br.org.oficinadasmeninas.infra.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import br.org.oficinadasmeninas.presentation.handler.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.oficinadasmeninas.domain.Response;
import br.org.oficinadasmeninas.domain.resources.Messages;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final GlobalExceptionHandler globalExceptionHandler;

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final ObjectMapper objectMapper;

	public SecurityConfig(
		AuthenticationProvider authenticationProvider,
		JwtAuthenticationFilter jwtAuthenticationFilter,
		ObjectMapper objectMapper
	, GlobalExceptionHandler globalExceptionHandler) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.objectMapper = objectMapper;
		this.globalExceptionHandler = globalExceptionHandler;
	}
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .csrf(AbstractHttpConfigurer::disable)
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/api/auth/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/sessions/present").permitAll()
				.requestMatchers(HttpMethod.POST, "/api/notifications/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/transparencies").permitAll()
				.requestMatchers(HttpMethod.GET, "/api/partners").permitAll()
				.anyRequest().authenticated()
	        )
	        .sessionManagement(session -> session
	            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        )
	        .authenticationProvider(authenticationProvider)
	        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
	        .exceptionHandling(ex -> ex
	        		.authenticationEntryPoint(customAuthenticationEntryPoint())
	                .accessDeniedHandler(customAccessDeniedHandler())
	            )
	        .cors(Customizer.withDefaults());

	    return http.build();
	}
	
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of(
				"http://localhost:4200",
                "http://localhost:8080",
                "https://oficinadasmeninas.org.br",
				"https://dev.oficinadasmeninas.org.br",
                "https://admin-dev.oficinadasmeninas.org.br",
                "https://admin.oficinadasmeninas.org.br",
                "http://dev.oficinadasmeninas.org.br",
                "http://admin-dev.oficinadasmeninas.org.br",
                "http://admin.oficinadasmeninas.org.br",
                "http://oficinadasmeninas.org.br")
				);
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
	
    @Bean
    AccessDeniedHandler customAccessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
        	response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");

            var body = new Response<Void>(Messages.ACCESS_DENIED, null);
            response.getWriter().write(objectMapper.writeValueAsString(body));
        };
    }

    @Bean
    AuthenticationEntryPoint customAuthenticationEntryPoint() {
    	return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");

            var body = new Response<Void>(Messages.UNAUTHORIZED, null);
            response.getWriter().write(objectMapper.writeValueAsString(body));
        };
	}

}

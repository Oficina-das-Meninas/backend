package br.org.oficinadasmeninas.infra.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.org.oficinadasmeninas.infra.auth.service.JwtService;
import br.org.oficinadasmeninas.presentation.shared.utils.CookieUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final String ACCESS_TOKEN = "access_token";
	 
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        if (!uri.startsWith("/api/auth")) {
            String accessToken = CookieUtils.getCookie(request, ACCESS_TOKEN);

            if (accessToken != null) {
                try {
    	            final String userEmail = jwtService.extractUsername(accessToken);
    	        	
    	            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	
    	            if (userEmail != null && authentication == null) {
    	                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
    	
    	                if (jwtService.isTokenValid(accessToken, userDetails)) {
    	                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
    	                            userDetails,
    	                            null,
    	                            userDetails.getAuthorities()
    	                    );
    	
    	                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    	                    SecurityContextHolder.getContext().setAuthentication(authToken);
    	                }
    	            }
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
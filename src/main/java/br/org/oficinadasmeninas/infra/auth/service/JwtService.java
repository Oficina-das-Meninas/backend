package br.org.oficinadasmeninas.infra.auth.service;

import br.org.oficinadasmeninas.infra.auth.UserDetailsCustom;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import br.org.oficinadasmeninas.infra.config.TokenExpirationProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;
	
	private final TokenExpirationProperties tokenExpirationProperties;
	
	public static enum PurposeTokenEnum {
		VERIFY_EMAIL, RESET_PASSWORD;
	}
	
	public JwtService(TokenExpirationProperties tokenExpirationProperties) {
		this.tokenExpirationProperties = tokenExpirationProperties;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public java.util.UUID extractUserId(String token) {
        String userId = extractClaim(token, claims -> claims.get("userId", String.class));
        if (userId != null && !userId.isEmpty()) {
            return java.util.UUID.fromString(userId);
        }

        throw new JwtException("userId claim is missing in token");
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	public String generateAdminSessionToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails, tokenExpirationProperties.getAdminSession());
	}
	
	public String generateUserSessionToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails, tokenExpirationProperties.getUserSession());
	}
	
	public String generateVerifyEmailToken(UserDetails userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("purpose", PurposeTokenEnum.VERIFY_EMAIL.name());
		
		return generateToken(extraClaims, userDetails, tokenExpirationProperties.getVerifyEmail());
	}
	
	public String generateResetPasswordToken(UserDetails userDetails) {
		Map<String, Object> extraClaims = new HashMap<>();
		extraClaims.put("purpose", PurposeTokenEnum.RESET_PASSWORD.name());
		
		return generateToken(extraClaims, userDetails, tokenExpirationProperties.getResetPassword());
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return buildToken(extraClaims, userDetails, expiration);
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (JwtException e) {
			return false;
		}
	}
	
	public boolean isTokenValidForPurpose(String token, UserDetails userDetails, PurposeTokenEnum requiredPurpose) {
		try {
			
			if (!isTokenValid(token, userDetails)) {
				return false;
			}

			final String purposeString = extractClaim(token, claims -> claims.get("purpose", String.class));

			if (purposeString == null || !purposeString.equals(requiredPurpose.name())) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public TokenExpirationProperties getTokenExpirationProperties() {
		return tokenExpirationProperties;
	}
	
	public String extractRole(String token) {
	    return extractClaim(token, claims -> claims.get("role", String.class));
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		long now = System.currentTimeMillis();

		Map<String, Object> claims = new HashMap<>(extraClaims);
		claims.put("sub", userDetails.getUsername());
		claims.put("iat", new Date(now));
		claims.put("exp", new Date(now + expiration));
		
		if (userDetails instanceof UserDetailsCustom customDetails) {
            if (customDetails.getId() != null) {
				claims.put("userId", customDetails.getId().toString());
			}
		}

		List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

		if (!roles.isEmpty()) {
	        claims.put("role", roles.getFirst());
	    }

		return Jwts.builder().claims(claims).signWith(getSignInKey(), Jwts.SIG.HS256).compact();
	}

	private Claims extractAllClaims(String token) {
		return jwtParser().parseSignedClaims(token).getPayload();
	}

	private JwtParser jwtParser() {
		return Jwts.parser().verifyWith(getSignInKey()).build();
	}

	private SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
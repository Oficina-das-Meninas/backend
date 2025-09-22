package br.org.oficinadasmeninas.infra.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	public long getExpirationTime() {
		return jwtExpiration;
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		try {
			final String username = extractUsername(token);
			return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
		} catch (JwtException e) {
			return false;
		}
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
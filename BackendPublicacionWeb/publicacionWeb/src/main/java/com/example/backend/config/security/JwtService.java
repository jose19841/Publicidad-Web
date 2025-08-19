package com.example.backend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Clase encargada de la creación, validación y extracción de datos de los JWTs (JSON Web Tokens).
 * Este servicio permite generar un token JWT, validar su autenticidad y extraer los datos del token.
 */
@Service
@Slf4j
public class JwtService {

    // Clave secreta para firmar el JWT, que se obtiene de las propiedades de configuración.
    @Value("${jwt.secret}")
    private String secretKey;

    public String extractUserName(String jwt) {
        try {
            String username = extractClaim(jwt, Claims::getSubject);
            log.debug("Se extrajo el username [{}] del token", username);
            return username;
        } catch (Exception e) {
            log.warn("No se pudo extraer el username del token: {}", e.getMessage());
            throw e;
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String jwt, UserDetails userDetails) {
        try {
            final String username = extractUserName(jwt);
            boolean valid = (username.equals(userDetails.getUsername()) && !isTokenExpired(jwt));

            if (valid) {
                log.info("Token válido para el usuario [{}]", username);
            } else {
                log.warn("Token inválido para el usuario [{}]", username);
            }

            return valid;
        } catch (Exception e) {
            log.error("Error al validar token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String jwt) {
        boolean expired = extractExpiration(jwt).before(new Date());
        if (expired) {
            log.warn("Token expirado detectado");
        }
        return expired;
    }

    private Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> claimsAdd, UserDetails userDetails) {
        claimsAdd.put("role", userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse(null));

        String token = Jwts.builder()
                .setClaims(claimsAdd)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1h
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        log.info("Se generó un nuevo token para el usuario [{}], rol: {}",
                userDetails.getUsername(), claimsAdd.get("role"));
        return token;
    }

    public <T> T extractClaim(String jwt, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(jwt);
        return claimsTFunction.apply(claims);
    }

    private Claims extractAllClaims(String jwt) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            log.error("Error al extraer claims del token: {}", e.getMessage());
            throw e;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

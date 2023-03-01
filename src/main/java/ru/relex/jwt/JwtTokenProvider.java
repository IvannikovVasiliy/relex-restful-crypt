package ru.relex.jwt;

import com.auth0.jwt.JWT;
import ru.relex.entity.Role;
import ru.relex.exception.JwtExpiredException;
import ru.relex.exception.TokenIsNotValidException;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expired}")
    private Long expired;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String username, Set<Role> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + expired);

        return Jwts
                .builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Authentication getAuth(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException("The jwt-token is expired. Refresh it");
        }
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer")) {
            return token.substring(7, token.length());
        }

        return null;
    }

    public List<String> getRoleNames(Set<Role> roles) {
        return roles
                .stream()
                .map(role -> role.name())
                .toList();
    }

    public void validateToken(String token) {
        try {
            JWT.decode(token);
        } catch (RuntimeException e) {
            throw new TokenIsNotValidException("The jwt-token is not valid");
        }

        long expiredDate = JWT.decode(token).getExpiresAt().getTime();
        long now = new Date().getTime();
        if (expiredDate < now-expired) {
            throw new JwtExpiredException("The jwt-token is expired. Refresh it");
        }
    }
}

package com.example.todoBack.service;

import io.jsonwebtoken.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

  @Value("${jwt.token.secret}")
  private String secret;

  @Value("${jwt.token.expired}")
  private long days;

  @PostConstruct
  protected void init() {
    secret = Base64.getEncoder().encodeToString(secret.getBytes());
  }


  public String createToken(Long id, String email) {
    Claims claims = Jwts.claims().setSubject(email);
    claims.put("id",id);

    Date now = new Date();
    Date validity = new Date(now.getTime() + days);

    return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
  }

  public String getEmail(String token){
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
  }

  public String resolveToken(@NotNull HttpServletRequest req) {
    String bearerToken = req.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7, bearerToken.length());
    }
    return "";
  }

  public boolean validateToken(String token) throws AuthenticationException {
    try {
      Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

      return !claims.getBody().getExpiration().before(new Date());
    } catch (JwtException | IllegalArgumentException e) {
      throw new AuthenticationException("JWT token is expired or invalid");
    }
  }

  public String emailForToken(HttpServletRequest req) throws AuthenticationException {
    String token = resolveToken(req);
    if (token == null) {
      throw new AuthenticationException("JWT token is expired or invalid");
    }
    boolean check = validateToken(token);
    if (!check) {
      throw new AuthenticationException("JWT token is expired or invalid");
    }

    return getEmail(token);
  }

}

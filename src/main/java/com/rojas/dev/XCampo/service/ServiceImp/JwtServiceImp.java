package com.rojas.dev.XCampo.service.ServiceImp;


import com.rojas.dev.XCampo.Auth.AuthResponse;
import com.rojas.dev.XCampo.exception.TokenExpiredException;
import com.rojas.dev.XCampo.service.Interface.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImp implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.secretKey}")
    private String KEY;

    @Value("${jwt.expiration}")
    private long accessTokenExpiration; // 15 minutos
    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiration; // 7 días

    @Override
    public AuthResponse getToken(UserDetails user, Long id_user) {
        String token = generateToken(new HashMap<>(), user.getUsername());
        String tokenRefresh = generateRefreshToken(user);
        return AuthResponse.builder().token(token).refreshToken(tokenRefresh).id_user(id_user).build();
    }

    @Override
    public String getUserNameFromToken(String token) {
        return getClaim(token,Claims::getSubject);
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String mail=getUserNameFromToken(token);
        return (mail.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return Jwts
                .builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Claims claims = getAllClaims(refreshToken);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException ex) {
            return false; // Token expirado
        } catch (JwtException ex) {
            throw new IllegalArgumentException("Refresh token inválido");
        }
    }

    @Override
    public String refreshAccessToken(String refreshToken) {
        if (!validateRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Refresh token inválido o expirado");
        }

        String username = getUserNameFromToken(refreshToken);
        Map<String, Object> extraClaims = new HashMap<>();
        return generateToken(extraClaims, username);
    }

    public String getUsernameFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    private String generateToken(Map<String, Object> extraClaims, String user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private Claims getAllClaims (String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException ex) {
            throw new TokenExpiredException("Token Expired: " + token);
        }
    }

    public <T> T getClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpired(String token){
        return getClaim(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        return getExpired(token).before(new Date());
    }
}

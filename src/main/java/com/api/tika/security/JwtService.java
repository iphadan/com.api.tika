package com.api.tika.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${spring.application.security.jwt.secretKey}")
    String secretKey;
  @Value("${spring.application.security.jwt.expiration}")
    Long jwtExpiration;
    public String extractUserName(String token) {
        System.out.println(token);
        return extractClaims(token, Claims::getSubject);
    }

    public Date getJwtExpiration(String token){
        return extractClaims(token,Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token).getBody();
    }
    public boolean isValidToken(String token,UserDetails userDetails){
        return extractUserName(token).equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return getJwtExpiration(token).before(new Date(System.currentTimeMillis()));
    }


    public String generateToken(UserDetails userDetails){

        return generateToken(new HashMap<String,Object>(),userDetails);
    }
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        return buildToken(extraClaims,userDetails);

    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails) {
var authorities = userDetails
        .getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .signWith(getSecretKey())
        .claim("authorities",authorities)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis()+ jwtExpiration) )
        .compact();

    }
    public Key getSecretKey(){
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);

    }

}

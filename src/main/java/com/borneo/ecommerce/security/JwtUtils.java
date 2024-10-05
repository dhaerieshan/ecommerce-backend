package com.borneo.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    private static  final String JWT_SECRET = "bF80myE0QiU/cp846ybXr4eyEDgEQbnh/YY+FZ7USlY=";
    private static final long JWT_EXPLORATION_MS = 86400000;

    private Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());

    public String generateJwtToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JWT_EXPLORATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJwt(token)
                .getBody()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(authToken);
            return true;
        }catch (JwtException e){
            System.out.println("Error occurred");
        }
        return false;
    }
}

package com.huancoder.market.security;



import com.huancoder.market.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {
    private String secret="2a741e7be5b86880249134af3700ab8b95d9125125ecb4374d99cd5690a821ba";
    public Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody();
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimResole){
        final Claims claims=extractAllClaims(token);
        return claimResole.apply(claims);
    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public String generatedClaim(String username, Collection<? extends GrantedAuthority> claims){

        return Jwts.builder()
                .claim("Authorization",claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() +60*60*24*10*1000)))
                .signWith(SignatureAlgorithm.HS256, secret).compact();

    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String username= extractUsername(token);
        return (username.equals(userDetails.getUsername()))?true:false;
    }

    public String generatedRefreshToken(User userSave) {
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userSave.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration((new Date(System.currentTimeMillis() + 60*60*24*10*1000)))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }
}

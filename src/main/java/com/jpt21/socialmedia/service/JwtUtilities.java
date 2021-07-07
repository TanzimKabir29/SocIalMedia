package com.jpt21.socialmedia.service;

import com.jpt21.socialmedia.model.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtilities {

    @Qualifier(value = "jwtSecret")
    private String SECRET;
    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    private static ConcurrentMap<String, String> userNameTokenMap = new ConcurrentHashMap<>();

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        } catch (SignatureException se) {
            log.info(se.getMessage());
            return new DefaultClaims();
        }
    }

    public boolean isFirstTimeLogin(String token){
        return getAllClaimsFromToken(token).get("isFirstTimeLogin",Boolean.class);
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("isFirstTimeLogin", userDetails.getAuthorities().stream().filter(a->a.toString().equals("FIRST_TIME_LOGIN_APP")).count()==1);
        return createClaimsToken(claims, userDetails.getUsername());
    }

    private String createClaimsToken(Map<String, Object> claims, String subject) {
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        userNameTokenMap.put(subject, token);
        return token;
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        if(token.equals(userNameTokenMap.get(username))) {
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } else {
            return false;
        }
    }

    //remove token from memory
    public boolean removeToken(String token) {
        final String username = getUsernameFromToken(token);
        if(token.equals(userNameTokenMap.get(username))) {
            return userNameTokenMap.remove(username) != null;
        } else {
            return false;
        }
    }
}

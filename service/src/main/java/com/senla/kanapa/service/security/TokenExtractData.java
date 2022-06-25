package com.senla.kanapa.service.security;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TokenExtractData {

    public LocalDateTime extractDateExpiration(String token) {
        var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        var parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

        var dateExpiration = LocalDateTime.ofInstant(parsedToken.getBody().getExpiration().toInstant(), ZoneId.systemDefault());
        ;
        return dateExpiration;
    }

    public Long extractUserIdFromToken(String token) {
        var signingKey = SecurityConstants.JWT_SECRET.getBytes();

        var parsedToken = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

        var userId = parsedToken
                .getBody()
                .getId();
        return Long.parseLong(userId);
    }
}

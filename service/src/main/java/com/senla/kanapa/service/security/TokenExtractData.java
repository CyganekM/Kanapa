package com.senla.kanapa.service.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Log4j2
public class TokenExtractData {

    public LocalDateTime extractDateExpiration(String token) {
        try {
            var signingKey = SecurityConstants.JWT_SECRET.getBytes();

            var parsedToken = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token.replace(SecurityConstants.TOKEN_PREFIX, ""));

            var dateExpiration = LocalDateTime.ofInstant(parsedToken.getBody().getExpiration().toInstant(), ZoneId.systemDefault());
            return dateExpiration;
        } catch (ExpiredJwtException exception) {
           log.debug(exception.getMessage());
        }
        return LocalDateTime.of(1, 1, 1, 1, 1);
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

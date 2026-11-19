package tftc.lch.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    SecretKey key;

    @BeforeEach void setUp() {
        key = Keys.hmacShaKeyFor("test-secret-key-for-jwt-testing-1234567890".getBytes(StandardCharsets.UTF_8));
    }

    @Test void generateAndParse() {
        String token = Jwts.builder().subject("1").claim("cv", 1L)
            .issuedAt(new java.util.Date())
            .expiration(new java.util.Date(System.currentTimeMillis() + 3600000))
            .signWith(key).compact();
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3);
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        assertEquals("1", claims.getSubject());
    }

    @Test void invalidTokenThrows() {
        assertThrows(Exception.class, () -> Jwts.parser().verifyWith(key).build().parseSignedClaims("bad.token.here"));
    }
}

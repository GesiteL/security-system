package tftc.lch.security.service;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import tftc.lch.security.infrastructure.security.JwtUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsImplTest {

    JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretStr", "test-secret-key-for-jwt-testing-1234567890");
        ReflectionTestUtils.setField(jwtUtils, "expireMs", 3600000L);
        jwtUtils.init();
    }

    @Test void generateAndParseToken() {
        String token = jwtUtils.generateToken(42L, 3L);
        assertNotNull(token);
        Claims claims = jwtUtils.parseToken(token);
        assertEquals("42", claims.getSubject());
        assertEquals(3L, claims.get("cv", Long.class));
    }

    @Test void getUserId() {
        String token = jwtUtils.generateToken(42L, 3L);
        assertEquals(42L, jwtUtils.getUserId(token));
    }

    @Test void getCredentialsVersion() {
        String token = jwtUtils.generateToken(42L, 3L);
        assertEquals(3L, jwtUtils.getCredentialsVersion(token));
    }

    @Test void invalidTokenThrows() {
        assertThrows(Exception.class, () -> jwtUtils.parseToken("bad.token.here"));
    }
}

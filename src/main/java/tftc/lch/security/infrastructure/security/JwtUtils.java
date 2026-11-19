package tftc.lch.security.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/** JWT 令牌工具（UC-02/03/25 依赖）。 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretStr;

    @Value("${jwt.expire-ms}")
    private long expireMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secretStr.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, Long credentialsVersion) {
        Date now = new Date();
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("cv", credentialsVersion)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expireMs))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        return Long.valueOf(parseToken(token).getSubject());
    }

    public Long getCredentialsVersion(String token) {
        return parseToken(token).get("cv", Long.class);
    }
}

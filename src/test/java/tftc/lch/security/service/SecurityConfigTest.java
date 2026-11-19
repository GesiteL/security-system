package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import tftc.lch.security.SecuritySystemApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = SecuritySystemApplication.class)
class SecurityConfigTest {

    @Autowired ApplicationContext ctx;

    @Test void securityFilterChainBeanExists() {
        assertTrue(ctx.containsBean("filterChain"));
    }

    @Test void passwordEncoderBeanExists() {
        PasswordEncoder encoder = ctx.getBean(PasswordEncoder.class);
        assertNotNull(encoder);
        String hash = encoder.encode("pass");
        assertTrue(encoder.matches("pass", hash));
    }

    @Test void securityFilterChainNotNull() {
        SecurityFilterChain chain = ctx.getBean(SecurityFilterChain.class);
        assertNotNull(chain);
    }
}

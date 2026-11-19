package tftc.lch.security;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class SecuritySystemApplicationTest {

    @Test
    void main() {
        String[] args = new String[]{};
        try (MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            mocked.when(() -> SpringApplication.run(SecuritySystemApplication.class, args))
                    .thenReturn(null);
            SecuritySystemApplication.main(args);
            mocked.verify(() -> SpringApplication.run(SecuritySystemApplication.class, args));
        }
    }
}

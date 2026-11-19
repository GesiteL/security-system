package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {
    @Test void domainExceptionHasCodeAndMessage() {
        tftc.lch.security.domain.shared.DomainException e = new tftc.lch.security.domain.shared.DomainException(1001, "测试异常");
        assertEquals(1001, e.getErrorCode());
        assertEquals("测试异常", e.getMessage());
    }
}

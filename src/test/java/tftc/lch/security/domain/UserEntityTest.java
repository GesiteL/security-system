package tftc.lch.security.domain;

import org.junit.jupiter.api.Test;
import tftc.lch.security.domain.user.User;
import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test void createUser() {
        User u = User.create("test", "hash", "Tester", "13800000000", "t@t.com");
        assertEquals("test", u.getAccount());
        assertEquals("Tester", u.getRealName());
        assertEquals(1, u.getStatus());
    }

    @Test void softDelete() {
        User u = User.create("u", "h", "U", "138", "e");
        u.setId(5);
        u.softDelete();
        assertTrue(u.isDeleted());
        assertEquals(5, u.getDeletedToken());
    }

    @Test void incrementCredentials() {
        User u = User.create("u", "h", "U", "138", "e");
        u.incrementCredentialsVersion();
        assertEquals(2, u.getCredentialsVersion());
    }

    @Test void updateInfo() {
        User u = User.create("u", "h", "U", "138", "e");
        u.updateInfo("NewName", "13900000001", "n@t.com", true);
        assertEquals("NewName", u.getRealName());
        assertEquals(0, u.getStatus());
        assertTrue(u.isDisabled());
    }
}

package tftc.lch.security.service;

import org.junit.jupiter.api.Test;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class BaseContextTest {
    @Test void setAndGetUserId() { BaseContext.setCurrentUserId(42L); assertEquals(42L,BaseContext.getCurrentUserId()); BaseContext.clear(); assertNull(BaseContext.getCurrentUserId()); }

    @Test void setAndGetPermissions() { var p = new java.util.HashSet<String>(); p.add("user:list"); BaseContext.setCurrentUserPermissions(p); assertTrue(BaseContext.hasPermission("user:list")); assertFalse(BaseContext.hasPermission("role:list")); BaseContext.clear(); }

    @Test void emptyPermissions() { BaseContext.clear(); assertFalse(BaseContext.hasPermission("any")); }

    @Test void getCurrentUserPermissionsWhenSet() { var p = new java.util.HashSet<String>(); p.add("role:list"); BaseContext.setCurrentUserPermissions(p); assertEquals(p, BaseContext.getCurrentUserPermissions()); assertTrue(BaseContext.getCurrentUserPermissions().contains("role:list")); BaseContext.clear(); }

    @Test void getCurrentUserPermissionsWhenNull() { BaseContext.clear(); var result = BaseContext.getCurrentUserPermissions(); assertNotNull(result); assertTrue(result.isEmpty()); }

    @Test void constructor() { assertNotNull(new BaseContext()); }

    @RequirePermission("user:list") void annotated() {}
    @Test void annotation() throws Exception { Method m = getClass().getDeclaredMethod("annotated"); RequirePermission rp = m.getAnnotation(RequirePermission.class); assertEquals("user:list",rp.value()); }
}

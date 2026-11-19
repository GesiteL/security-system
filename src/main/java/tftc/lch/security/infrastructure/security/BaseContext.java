package tftc.lch.security.infrastructure.security;

import java.util.HashSet;
import java.util.Set;

/** ThreadLocal 当前用户上下文 */
public class BaseContext {
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Set<String>> CURRENT_USER_PERMISSIONS = new ThreadLocal<>();

    public static void setCurrentUserId(Long id) { CURRENT_USER_ID.set(id); }
    public static Long getCurrentUserId() { return CURRENT_USER_ID.get(); }

    public static void setCurrentUserPermissions(Set<String> perms) { CURRENT_USER_PERMISSIONS.set(perms); }
    public static Set<String> getCurrentUserPermissions() {
        Set<String> p = CURRENT_USER_PERMISSIONS.get();
        return p != null ? p : new HashSet<>();
    }
    public static boolean hasPermission(String perm) {
        Set<String> p = CURRENT_USER_PERMISSIONS.get();
        return p != null && p.contains(perm);
    }
    public static void clear() {
        CURRENT_USER_ID.remove();
        CURRENT_USER_PERMISSIONS.remove();
    }
}

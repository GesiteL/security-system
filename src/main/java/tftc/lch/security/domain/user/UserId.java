package tftc.lch.security.domain.user;

/** 用户 ID 值对象（不可变）。 */
public record UserId(int value) {
    public UserId {
        if (value <= 0) throw new IllegalArgumentException("UserId must be positive");
    }
}

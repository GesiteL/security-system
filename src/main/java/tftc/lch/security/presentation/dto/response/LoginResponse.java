package tftc.lch.security.presentation.dto.response;

/** 登入成功响应 DTO（UC-02）。 */
public class LoginResponse {
    private String token;
    private int userId;
    private String account;

    public static LoginResponse of(String token, int userId, String account) {
        LoginResponse r = new LoginResponse();
        r.token = token;
        r.userId = userId;
        r.account = account;
        return r;
    }

    public String getToken() { return token; }
    public int getUserId() { return userId; }
    public String getAccount() { return account; }
}

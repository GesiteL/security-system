package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tftc.lch.security.application.auth.*;
import tftc.lch.security.infrastructure.security.JwtUtils;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.LoginResponse;

@RestController
public class AuthControllerImpl implements AuthController {
    private final AuthApplicationService authService;
    private final JwtUtils jwtUtils;

    public AuthControllerImpl(AuthApplicationService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @Override public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        RegisterCommand cmd = new RegisterCommand(request.getAccount(), request.getPassword(), request.getRealName(), request.getPhone(), request.getEmail());
        authService.register(cmd);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(new LoginCommand(request.getAccount(), request.getPassword()));
        Long uid = jwtUtils.getUserId(token);
        return ResponseEntity.ok(LoginResponse.of(token, uid.intValue(), request.getAccount()));
    }

    @Override public ResponseEntity<Void> logout() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            String auth = attrs.getRequest().getHeader("Authorization");
            if (auth != null && auth.startsWith("Bearer ")) {
                try { Long uid = jwtUtils.getUserId(auth.substring(7)); authService.logout(new LogoutCommand(uid.intValue())); }
                catch (Exception e) {}
            }
        }
        return ResponseEntity.noContent().build();
    }

    @Override public ResponseEntity<Void> sendResetCode(@RequestBody SendResetCodeRequest request) {
        authService.sendPasswordResetCode(new ForgotPasswordCommand(request.getContactType(), request.getContactValue()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override public ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.toCommand());
        return ResponseEntity.noContent().build();
    }
}
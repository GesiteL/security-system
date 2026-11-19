package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.user.*;
import tftc.lch.security.domain.user.User;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.stream.Collectors;

@RestController
public class UserControllerImpl implements UserController {
    private final UserApplicationService userService;
    public UserControllerImpl(UserApplicationService userService) { this.userService = userService; }

    @Override @RequirePermission("GET:/api/v1/users")
    public ResponseEntity<PageResponse<UserResponse>> listUsers(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize, @RequestParam(required=false) String accountKeyword, @RequestParam(required=false) String realNameKeyword, @RequestParam(required=false) String phoneKeyword) {
        if (page < 1) page = 1;
        if (pageSize < 1) pageSize = 20;
        GetUserQuery q = new GetUserQuery(accountKeyword, realNameKeyword, phoneKeyword, null, (page-1)*pageSize, pageSize);
        var users = userService.listUsers(q); int total = userService.countUsers(q);
        var records = users.stream().map(UserResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(records, total, page, pageSize));
    }

    @Override @ResponseStatus(HttpStatus.CREATED) @RequirePermission("POST:/api/v1/users")
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        Long opId = BaseContext.getCurrentUserId();
        User user = userService.createUser(request.toCommand(opId != null ? opId.intValue() : 0));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(user));
    }

    @Override @RequirePermission("PATCH:/api/v1/users")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int userId, @RequestBody UpdateUserRequest request) {
        Long opId = BaseContext.getCurrentUserId();
        User user = userService.updateUser(request.toCommand(userId, opId != null ? opId.intValue() : 0));
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @Override @ResponseStatus(HttpStatus.NO_CONTENT) @RequirePermission("DELETE:/api/v1/users")
    public ResponseEntity<Void> deleteUser(@PathVariable int userId) {
        Long opId = BaseContext.getCurrentUserId();
        userService.deleteUser(new DeleteUserCommand(userId, opId != null ? opId.intValue() : 0));
        return ResponseEntity.noContent().build();
    }
}
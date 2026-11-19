package tftc.lch.security.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.application.permission.*;
import tftc.lch.security.domain.permission.SysFunction;
import tftc.lch.security.infrastructure.annotation.RequirePermission;
import tftc.lch.security.infrastructure.security.BaseContext;
import tftc.lch.security.presentation.dto.request.*;
import tftc.lch.security.presentation.dto.response.*;
import java.util.stream.Collectors;

@RestController
public class FunctionControllerImpl implements FunctionController {
    private final PermissionApplicationService ps;
    public FunctionControllerImpl(PermissionApplicationService ps) { this.ps = ps; }
    @Override @RequirePermission("GET:/api/v1/functions")
    public ResponseEntity<PageResponse<FunctionResponse>> listFunctions(@RequestParam(defaultValue="1") int page, @RequestParam(defaultValue="20") int pageSize, @RequestParam(required=false) String nameKeyword, @RequestParam(required=false) String module) {
        GetFunctionQuery q = new GetFunctionQuery(nameKeyword, module, (page-1)*pageSize, pageSize);
        var fs = ps.listFunctions(q); int total = ps.countFunctions(q);
        var recs = fs.stream().map(FunctionResponse::fromDomain).collect(Collectors.toList());
        return ResponseEntity.ok(PageResponse.of(recs, total, page, pageSize));
    }
    @Override @RequirePermission("POST:/api/v1/functions")
    public ResponseEntity<FunctionResponse> registerFunction(@RequestBody RegisterFunctionRequest r) {
        Long op = BaseContext.getCurrentUserId();
        SysFunction f = ps.registerFunction(r.toCommand(op != null ? op.intValue() : 0));
        return ResponseEntity.status(HttpStatus.CREATED).body(FunctionResponse.fromDomain(f));
    }
    @Override @RequirePermission("PATCH:/api/v1/functions")
    public ResponseEntity<FunctionResponse> updateFunction(@PathVariable int functionId, @RequestBody UpdateFunctionRequest r) {
        Long op = BaseContext.getCurrentUserId();
        SysFunction f = ps.updateFunction(r.toCommand(functionId, op != null ? op.intValue() : 0));
        return ResponseEntity.ok(FunctionResponse.fromDomain(f));
    }
    @Override @RequirePermission("DELETE:/api/v1/functions")
    public ResponseEntity<Void> deleteFunction(@PathVariable int functionId) { ps.deleteFunction(new DeleteFunctionCommand(functionId, 0)); return ResponseEntity.noContent().build(); }
}

package tftc.lch.security.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tftc.lch.security.presentation.dto.request.RegisterFunctionRequest;
import tftc.lch.security.presentation.dto.request.UpdateFunctionRequest;
import tftc.lch.security.presentation.dto.response.FunctionResponse;
import tftc.lch.security.presentation.dto.response.PageResponse;

/**
 * 系统功能 Controller 接口（UC-17 至 UC-20）。
 * 由 Tester/Developer 实现，加 {@code @RestController}。
 *
 * @see <a href="../../../docs/architecture/arch17查询系统功能.html#api-GET-functions">GET /api/v1/functions</a>
 * @see <a href="../../../docs/architecture/arch20删除系统功能.html#api-DELETE-functions-id">DELETE /api/v1/functions/{functionId}</a>
 */
@RequestMapping("/api/v1/functions")
public interface FunctionController {

    @GetMapping
    ResponseEntity<PageResponse<FunctionResponse>> listFunctions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(required = false) String nameKeyword,
            @RequestParam(required = false) String module
    );

    @PostMapping
    ResponseEntity<FunctionResponse> registerFunction(@RequestBody RegisterFunctionRequest request);

    @PatchMapping("/{functionId}")
    ResponseEntity<FunctionResponse> updateFunction(@PathVariable int functionId,
                                                      @RequestBody UpdateFunctionRequest request);

    @DeleteMapping("/{functionId}")
    ResponseEntity<Void> deleteFunction(@PathVariable int functionId);
}

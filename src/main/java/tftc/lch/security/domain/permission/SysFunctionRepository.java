package tftc.lch.security.domain.permission;
import org.apache.ibatis.annotations.Mapper;
import java.util.Optional;
import java.util.List;
@Mapper
public interface SysFunctionRepository {
    Optional<SysFunction> findById(int functionId);
    List<SysFunction> findAll(String nameKeyword, String module, int page, int pageSize);
    int countAll(String nameKeyword, String module);
    int save(SysFunction function);
    int update(SysFunction function);
    boolean existsByName(String functionName);
    boolean existsByNameExcludeId(String functionName, int excludeId);
}
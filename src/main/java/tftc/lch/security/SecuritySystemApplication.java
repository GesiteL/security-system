package tftc.lch.security;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan(basePackages = "tftc.lch.security.domain", annotationClass = Mapper.class)
@EnableAspectJAutoProxy
public class SecuritySystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecuritySystemApplication.class, args);
    }
}

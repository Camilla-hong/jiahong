package hzu.zhang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("hzu.zhang.dao")
public class Starter {
    public static void main(String[] args) {

        SpringApplication.run(Starter.class);
    }
}

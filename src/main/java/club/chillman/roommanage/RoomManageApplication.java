package club.chillman.roommanage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author NIU
 * @createTime 2021/3/30 20:38
 */
@SpringBootApplication
@ComponentScan(basePackages = {"club.chillman"})
@MapperScan("club.chillman.roommanage.mapper")
public class RoomManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(RoomManageApplication.class, args);
    }
}

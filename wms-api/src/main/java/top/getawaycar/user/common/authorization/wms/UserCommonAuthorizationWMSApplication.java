package top.getawaycar.user.common.authorization.wms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"top.getawaycar.user.common.authorization.wms.dao", "top.getawaycar.user.common.authorization.dao"})
public class UserCommonAuthorizationWMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCommonAuthorizationWMSApplication.class, args);
    }

}

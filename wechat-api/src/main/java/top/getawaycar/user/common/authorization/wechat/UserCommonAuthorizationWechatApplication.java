package top.getawaycar.user.common.authorization.wechat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"top.getawaycar.user.common.authorization.wechat.dao", "top.getawaycar.user.common.authorization.dao"})
public class UserCommonAuthorizationWechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCommonAuthorizationWechatApplication.class, args);
    }

}

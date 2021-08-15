package top.getawaycar.user.common.authorization.wechat.controller.common;

import org.springframework.web.bind.annotation.*;
import top.getawaycar.user.common.authorization.wechat.service.ISubscriberService;

/**
 * <p>Title: RegisterController</p>
 * <p>Description: 用户注册类 该类中包含常用的用户注册接口</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
@RestController
@RequestMapping("login")
public class LoginController {

    private final ISubscriberService userService;

    public LoginController(ISubscriberService userService) {
        this.userService = userService;
    }

    @GetMapping("ma")
    public String maLogin(@RequestHeader("AppId") String appId, @RequestParam String code) {
        return userService.maLogin(appId, code);
    }

    @GetMapping("mp")
    public String mpLogin(@RequestHeader("AppId") String appId, @RequestParam String code) {
        return userService.mpLogin(appId, code);
    }

}

package top.getawaycar.user.common.authorization.wechat.controller.common;

import org.springframework.web.bind.annotation.*;
import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppRegisterDTO;
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
@RequestMapping("register")
public class RegisterController {

    private final ISubscriberService userService;

    public RegisterController(ISubscriberService userService) {
        this.userService = userService;
    }

    @PostMapping("ma/token")
    public String maRegisterWithToken(@RequestHeader("AppId") String appId, @RequestBody MiniAppRegisterDTO miniAppRegisterDTO) {
        return userService.maUserRegisterWithToken(appId, miniAppRegisterDTO);
    }

    @PostMapping("ma/none")
    public void maRegisterWithNone(@RequestHeader("AppId") String appId, @RequestBody MiniAppRegisterDTO miniAppRegisterDTO) {
        userService.maUserRegister(appId, miniAppRegisterDTO);
    }

    @GetMapping("mp/token")
    public String mpRegisterUserInformation(@RequestHeader("AppId") String appId, @RequestParam String code) {
        return userService.mpUserRegisterWithToken(appId, code);
    }

    @GetMapping("mp/none")
    public void mpRegisterUserInformationWithNone(@RequestHeader("AppId") String appId, @RequestParam String code) {
        userService.mpUserRegister(appId, code);
    }

}

package top.getawaycar.user.common.authorization.wms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import top.getawaycar.user.common.authorization.wms.pojo.dto.LoginRequestDTO;
import top.getawaycar.user.common.authorization.wms.service.IManageAccountService;
import top.getawaycar.user.common.authorization.wms.util.CommonRedisUtil;

/**
 * <p>Title: ManageAccountSurfaceController</p>
 * <p>Description: 后台系统用户统一操作</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
@RestController
public class ManageAccountSurfaceController {

    private IManageAccountService manageAccountService;
    private CommonRedisUtil commonRedisUtil;

    @Autowired
    public void setCommonRedisUtil(CommonRedisUtil commonRedisUtil) {
        this.commonRedisUtil = commonRedisUtil;
    }

    @Autowired
    public void setManageAccountService(IManageAccountService manageAccountService) {
        this.manageAccountService = manageAccountService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequest) {
        return manageAccountService.login(loginRequest);
    }

    @PostMapping("/logout")
    public void logout() {
        manageAccountService.logout();
    }


}

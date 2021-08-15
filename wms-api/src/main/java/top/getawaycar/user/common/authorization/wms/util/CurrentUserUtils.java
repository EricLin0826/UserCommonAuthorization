package top.getawaycar.user.common.authorization.wms.util;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountPO;
import top.getawaycar.user.common.authorization.wms.service.IManageAccountService;

/**
 * @author shuang.kou
 * @description 获取当前请求的用户
 */
@Component
public class CurrentUserUtils {

    private IManageAccountService userService;

    @Autowired
    public void setUserService(IManageAccountService userService) {
        this.userService = userService;
    }

    public UmsManageAccountPO getCurrentUser() {
        return userService.getManageAccountByUsername(getCurrentUserName());
    }

    private  String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}

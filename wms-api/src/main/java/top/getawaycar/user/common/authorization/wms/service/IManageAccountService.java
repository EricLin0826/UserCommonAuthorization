package top.getawaycar.user.common.authorization.wms.service;

import top.getawaycar.user.common.authorization.domain.UmsManageAccountPO;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountRolePO;
import top.getawaycar.user.common.authorization.wms.pojo.dto.LoginRequestDTO;
import top.getawaycar.user.common.authorization.wms.pojo.dto.ManageAccountJWTDTO;

import java.util.List;

/**
 * <p>Title: IManageAccountService</p>
 * <p>Description: 后台用户服务类</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
public interface IManageAccountService {

    ManageAccountJWTDTO getManageAccountJWTDTOByUsername(String username);

    UmsManageAccountPO getManageAccountByUsername(String username);

    List<UmsManageAccountRolePO> listManageAccountRoleByAccountId(Long accountId);

    String login(LoginRequestDTO loginRequestDTO);

    void logout();

}

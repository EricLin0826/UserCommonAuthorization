package top.getawaycar.user.common.authorization.wms.factory;

import org.springframework.security.core.GrantedAuthority;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountPO;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountRolePO;
import top.getawaycar.user.common.authorization.wms.pojo.dto.ManageAccountJWTDTO;
import top.getawaycar.user.common.authorization.wms.pojo.dto.ManageAccountRoleDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: ManageAccountJWTDTOFactory</p>
 * <p>Description: ManageAccountJWTDTO 工厂类</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
public class ManageAccountJWTDTOFactory {

    public static ManageAccountJWTDTO create(UmsManageAccountPO user, List<UmsManageAccountRolePO> roles) {
        return new ManageAccountJWTDTO(
                user.getId(),
                user.getManageUsername(),
                user.getLoginPassword(),
                user.getManageEmail(),
                user.getManageTelephone(),
                user.getManageStatus(),
                getRoles(roles)
        );
    }


    public static List<ManageAccountRoleDTO> getRoles(List<UmsManageAccountRolePO> roles) {
        List<ManageAccountRoleDTO> tempRoles = new ArrayList<>();
        for (UmsManageAccountRolePO role : roles) {
            tempRoles.add(new ManageAccountRoleDTO(role.getRoleName()));
        }
        return tempRoles;
    }

}

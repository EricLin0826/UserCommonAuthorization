package top.getawaycar.user.common.authorization.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import top.getawaycar.user.common.authorization.dao.UmsManageAccountDAO;
import top.getawaycar.user.common.authorization.dao.UmsManageAccountRoleDAO;
import top.getawaycar.user.common.authorization.dao.UmsManageAccountRoleRelateDAO;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountPO;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountRolePO;
import top.getawaycar.user.common.authorization.domain.UmsManageAccountRoleRelatePO;
import top.getawaycar.user.common.authorization.utils.QueryWrapperUtils;
import top.getawaycar.user.common.authorization.wms.factory.ManageAccountJWTDTOFactory;
import top.getawaycar.user.common.authorization.wms.pojo.dto.LoginRequestDTO;
import top.getawaycar.user.common.authorization.wms.pojo.dto.ManageAccountJWTDTO;
import top.getawaycar.user.common.authorization.wms.service.IManageAccountService;
import top.getawaycar.user.common.authorization.wms.util.CommonRedisUtil;
import top.getawaycar.user.common.authorization.wms.util.CurrentUserUtils;
import top.getawaycar.user.common.authorization.wms.util.JwtTokenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: ManageAccountServiceImpl</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
@Service
public class ManageAccountServiceImpl implements IManageAccountService {

    private UmsManageAccountDAO umsManageAccountDAO;

    private UmsManageAccountRoleDAO umsManageAccountRoleDAO;

    private UmsManageAccountRoleRelateDAO umsManageAccountRoleRelateDAO;

    @Autowired
    public void setUmsManageAccountDAO(UmsManageAccountDAO umsManageAccountDAO) {
        this.umsManageAccountDAO = umsManageAccountDAO;
    }

    @Autowired
    public void setUmsManageAccountRoleDAO(UmsManageAccountRoleDAO umsManageAccountRoleDAO) {
        this.umsManageAccountRoleDAO = umsManageAccountRoleDAO;
    }

    @Autowired
    public void setUmsManageAccountRoleRelateDAO(UmsManageAccountRoleRelateDAO umsManageAccountRoleRelateDAO) {
        this.umsManageAccountRoleRelateDAO = umsManageAccountRoleRelateDAO;
    }

    @Override
    public ManageAccountJWTDTO getManageAccountJWTDTOByUsername(String username) {
        UmsManageAccountPO umsManageAccountPO = this.getManageAccountByUsername(username);
        List<UmsManageAccountRolePO> umsManageAccountRoles = this.listManageAccountRoleByAccountId(umsManageAccountPO.getId());
        return ManageAccountJWTDTOFactory.create(umsManageAccountPO, umsManageAccountRoles);
    }

    @Override
    public UmsManageAccountPO getManageAccountByUsername(String username) {
        //查询用户
        QueryWrapper<UmsManageAccountPO> manageAccountQueryWrapper = QueryWrapperUtils.getQueryWrapper();
        manageAccountQueryWrapper.eq("manage_username", username);
        return umsManageAccountDAO.selectOne(manageAccountQueryWrapper);
    }

    @Override
    public List<UmsManageAccountRolePO> listManageAccountRoleByAccountId(Long accountId) {
        //查询用户对应的角色ID
        QueryWrapper<UmsManageAccountRoleRelatePO> manageAccountRoleRelateQueryWrapper = QueryWrapperUtils.getQueryWrapper();
        manageAccountRoleRelateQueryWrapper.eq("manage_account_id", accountId);
        manageAccountRoleRelateQueryWrapper.select("manage_role_id");
        List<Object> roleId = umsManageAccountRoleRelateDAO.selectObjs(manageAccountRoleRelateQueryWrapper);

        //查询角色
        QueryWrapper<UmsManageAccountRolePO> manageAccountRoleQueryWrapper = QueryWrapperUtils.getQueryWrapper();
        manageAccountRoleQueryWrapper.in("id", roleId);
        List<UmsManageAccountRolePO> umsManageAccountRoles = umsManageAccountRoleDAO.selectList(manageAccountRoleQueryWrapper);
        return umsManageAccountRoles;
    }

    private JwtTokenUtils jwtTokenUtil;

    private AuthenticationManager authenticationManager;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public boolean check(String currentPassword, String password) {
        return bCryptPasswordEncoder.matches(currentPassword, password);
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UmsManageAccountPO account = this.getManageAccountByUsername(username);
        List<UmsManageAccountRolePO> umsManageAccountRoles = this.listManageAccountRoleByAccountId(account.getId());
        List<String> roles = new ArrayList<>();
        for (UmsManageAccountRolePO umsManageAccountRole : umsManageAccountRoles) {
            roles.add(umsManageAccountRole.getRoleName());
        }
        final String token = JwtTokenUtils.createToken(account.getManageUsername(), account.getId().toString(), roles, false);
        commonRedisUtil.set(account.getId().toString(), token);
        return token;
    }


    private CurrentUserUtils currentUserUtils;

    @Autowired
    public void setCurrentUserUtils(CurrentUserUtils currentUserUtils) {
        this.currentUserUtils = currentUserUtils;
    }

    @Override
    public void logout() {
        commonRedisUtil.del(currentUserUtils.getCurrentUser().getId().toString());
    }

    private CommonRedisUtil commonRedisUtil;

    @Autowired
    public void setCommonRedisUtil(CommonRedisUtil commonRedisUtil) {
        this.commonRedisUtil = commonRedisUtil;
    }
}

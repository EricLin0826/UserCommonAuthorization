package top.getawaycar.user.common.authorization.wms.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>Title: ManageAccountJWTDTO</p>
 * <p>Description: 后台管理用户JWT DTO</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
@Data
@ToString
public class ManageAccountJWTDTO implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final String telephone;
    private final Integer manageStatus;
    private final List<ManageAccountRoleDTO> roles;

    public ManageAccountJWTDTO(Long id, String username, String password, String email, String telephone, Integer manageStatus, List<ManageAccountRoleDTO> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.telephone = telephone;
        this.manageStatus = manageStatus;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (ManageAccountRoleDTO role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.manageStatus == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

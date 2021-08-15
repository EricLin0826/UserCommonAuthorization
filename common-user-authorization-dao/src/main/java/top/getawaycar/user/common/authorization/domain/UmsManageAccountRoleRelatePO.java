package top.getawaycar.user.common.authorization.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("ums_manage_account_role_relate")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UmsManageAccountRoleRelatePO {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long manageAccountId;
    private Long manageRoleId;
    @TableField(fill = FieldFill.INSERT)
    private Integer dataStatus;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long lastUpdateTime;

}

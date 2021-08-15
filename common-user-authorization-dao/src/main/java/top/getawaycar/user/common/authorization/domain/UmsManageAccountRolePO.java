package top.getawaycar.user.common.authorization.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("ums_manage_account_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UmsManageAccountRolePO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String roleName;
    @TableField(fill = FieldFill.INSERT)
    private Long dataStatus;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long lastUpdateTime;

}

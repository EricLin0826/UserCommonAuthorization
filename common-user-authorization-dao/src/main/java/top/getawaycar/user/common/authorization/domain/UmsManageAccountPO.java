package top.getawaycar.user.common.authorization.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("ums_manage_account")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UmsManageAccountPO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String manageProfile;
    private Integer manageSex;
    private String manageUsername;
    private String manageRealName;
    private String manageTelephone;
    private String loginPassword;
    private String manageEmail;
    private Integer manageStatus;
    @TableField(fill = FieldFill.INSERT)
    private Long dataStatus;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long lastUpdateTime;

}

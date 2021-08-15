package top.getawaycar.user.common.authorization.domain;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("ums_subscriber")
public class UmsSubscriberPO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String wechatUnionId;
    private String wechatOpenId;
    private String wechatNickName;
    private String wechatGender;
    private String wechatLanguage;
    private String wechatCity;
    private String wechatProvince;
    private String wechatCountry;
    private String wechatAvatarUrl;
    /**
     * 类型(0小程序,1公众号)
     */
    private Integer wechatType;
    /**
     * AppId
     */
    private String wechatAppId;
    /**
     * 电话
     */
    private String wechatTelephone;
    /**
     * 电话(脱敏)
     */
    private String wechatDesensitizationTelephone;
    /**
     * 注册时间
     */
    private Long registerTime;
    /**
     * 最后一次登录时间
     */
    private Long lastLoginTime;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Long lastUpdateTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer dataStatus;


}

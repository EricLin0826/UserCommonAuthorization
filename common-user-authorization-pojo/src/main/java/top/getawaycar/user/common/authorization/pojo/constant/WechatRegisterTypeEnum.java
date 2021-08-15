package top.getawaycar.user.common.authorization.pojo.constant;

/**
 * <p>Title: WechatRegisterTypeEnum</p>
 * <p>Description: 用户注册类型枚举</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public enum WechatRegisterTypeEnum {
    MINI_APP(0, "小程序注册"), MEDIA_PLATFORM(1, "公众号注册");
    private Integer code;
    private String zhCn;

    WechatRegisterTypeEnum(Integer code, String zhCn) {
        this.code = code;
        this.zhCn = zhCn;
    }

    public Integer getCode() {
        return code;
    }
}

package top.getawaycar.user.common.authorization.pojo.constant;

/**
 * <p>Title: DataStatusEnum</p>
 * <p>Description: 数据状态枚举</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public enum DataStatusEnum {

    DISABLE(0, "数据删除"), ENABLE(1, "数据未删除");
    private Integer code;
    private String zhCn;

    DataStatusEnum(Integer code, String zhCn) {
        this.code = code;
        this.zhCn = zhCn;
    }

    public Integer getCode() {
        return code;
    }

}

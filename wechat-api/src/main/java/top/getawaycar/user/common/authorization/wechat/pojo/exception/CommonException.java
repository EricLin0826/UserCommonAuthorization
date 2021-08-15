package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: CommonException</p>
 * <p>Description: 系统内部错误</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */

public abstract class CommonException extends RuntimeException {

    private Integer code;

    public CommonException(String message, Integer code) {
        super(message);
        this.code = code;
    }
}

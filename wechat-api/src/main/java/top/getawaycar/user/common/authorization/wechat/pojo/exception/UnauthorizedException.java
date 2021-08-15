package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: UnauthorizedException</p>
 * <p>Description: 未授权错误</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public class UnauthorizedException extends CommonException {
    public UnauthorizedException(String message) {
        super("未授权错误:" + message, 5006);
    }
}

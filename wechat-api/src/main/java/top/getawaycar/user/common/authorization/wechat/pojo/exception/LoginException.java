package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: LoginRegisterException</p>
 * <p>Description: 用户登录异常</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public class LoginException extends CommonException {
    public LoginException(String message) {
        super("登录错误:" + message, 5004);
    }
}

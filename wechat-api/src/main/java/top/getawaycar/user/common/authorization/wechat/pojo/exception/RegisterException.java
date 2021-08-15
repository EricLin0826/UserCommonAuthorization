package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: RegisterException</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
public class RegisterException extends CommonException {

    public RegisterException(String message) {
        super("注册错误:" + message, 5001);
    }
}

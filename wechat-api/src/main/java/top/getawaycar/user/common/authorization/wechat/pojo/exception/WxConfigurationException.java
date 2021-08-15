package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: WxConfigurationException</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
public class WxConfigurationException extends CommonException {

    public WxConfigurationException(String message) {
        super("配置错误:" + message, 5002);
    }
}

package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: ThirdPartyAPIException</p>
 * <p>Description: 第三方异常</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public class ThirdPartyAPIException extends CommonException {
    public ThirdPartyAPIException(String message) {
        super("第三方API错误:" + message, 5005);
    }
}

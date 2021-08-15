package top.getawaycar.user.common.authorization.wechat.pojo.exception;

/**
 * <p>Title: SubscriberInformationUpdateException</p>
 * <p>Description: 用户信息更新失败</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
public class SubscriberInformationUpdateException extends CommonException {
    public SubscriberInformationUpdateException(String message) {
        super("用户信息更新错误:" + message, 5003);
    }
}

package top.getawaycar.user.common.authorization.wechat.configuration.common;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Title: RedisMagicData</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
@Data
@ConfigurationProperties(prefix = "redis.prefix.subscriber")
@ToString
public class SubscriberRedisPrefixProperties {

    private String token;
    private String information;
    /**
     * Subscriber Id
     * 用户ID
     */
    private String sId;

}

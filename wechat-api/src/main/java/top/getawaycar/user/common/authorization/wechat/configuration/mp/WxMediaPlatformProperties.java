package top.getawaycar.user.common.authorization.wechat.configuration.mp;

import top.getawaycar.user.common.authorization.wechat.utils.JsonUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * <p>Title: WxMediaPlatformProperties</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/04
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wechat.media.platform")
public class WxMediaPlatformProperties {

    /**
     * 是否使用redis存储access token
     */
    private boolean useRedis;

    /**
     * redis 配置
     */
    private RedisConfig redis;

    @Data
    public static class RedisConfig {
        /**
         * redis服务器 主机地址
         */
        private String host;

        /**
         * redis服务器 端口号
         */
        private Integer port;

        /**
         * redis服务器 密码
         */
        private String password;

        /**
         * redis 服务连接超时时间
         */
        private Integer timeout;
    }

    /**
     * 多个公众号配置信息
     */
    private List<MpConfig> configs;

    @Data
    public static class MpConfig {
        /**
         * 设置微信公众号的appid
         */
        private String appId;

        /**
         * 设置微信公众号的app secret
         */
        private String secret;

        /**
         * 设置微信公众号的token
         */
        private String token;

        /**
         * 设置微信公众号的EncodingAESKey
         */
        private String aesKey;


        /**
         * 只允许用户在一处登录
         */
        private boolean exclusive;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}

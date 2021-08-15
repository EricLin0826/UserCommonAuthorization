package top.getawaycar.user.common.authorization.wechat.utils;

import top.getawaycar.user.common.authorization.domain.UmsSubscriberPO;
import top.getawaycar.user.common.authorization.wechat.configuration.ma.WxMiniAppProperties;
import org.springframework.stereotype.Component;
import top.getawaycar.user.common.authorization.wechat.configuration.common.SubscriberRedisPrefixProperties;
import top.getawaycar.user.common.authorization.wechat.configuration.mp.WxMediaPlatformProperties;
import top.getawaycar.user.common.authorization.pojo.constant.RedisKeyAndValueMagicData;
import top.getawaycar.user.common.authorization.wechat.pojo.exception.WxConfigurationException;

/**
 * <p>Title: SubscriberRedisUtils</p>
 * <p>Description: Subscriber Redis操作类</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
@Component
public class SubscriberRedisUtils {

    private final RedisUtils redisUtils;
    private final SubscriberRedisPrefixProperties subscriberRedisPrefixProperties;
    private final WxMiniAppProperties maProperties;
    private final WxMediaPlatformProperties mpProperties;

    public SubscriberRedisUtils(RedisUtils redisUtils, SubscriberRedisPrefixProperties subscriberRedisPrefixProperties, WxMiniAppProperties maProperties, WxMediaPlatformProperties mpProperties) {
        this.redisUtils = redisUtils;
        this.subscriberRedisPrefixProperties = subscriberRedisPrefixProperties;
        this.maProperties = maProperties;
        this.mpProperties = mpProperties;
    }

    public boolean saveOpenIdSubscriberTokenAndInformationCache(String appId, Long subscriberId, String token, UmsSubscriberPO umsSubscriber) {
        final String TOKEN_PREFIX = subscriberRedisPrefixProperties.getToken() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + subscriberId;
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;

        WxMiniAppProperties.MaConfig maConfig = null;
        for (int i = 0; i < maProperties.getConfigs().size(); i++) {
            WxMiniAppProperties.MaConfig propertiesItem = maProperties.getConfigs().get(i);
            if (propertiesItem.getAppId().equals(appId)) {
                maConfig = propertiesItem;
            }
        }

        WxMediaPlatformProperties.MpConfig mpConfig = null;
        for (int i = 0; i < mpProperties.getConfigs().size(); i++) {
            WxMediaPlatformProperties.MpConfig propertiesItem = mpProperties.getConfigs().get(i);
            if (propertiesItem.getAppId().equals(appId)) {
                mpConfig = propertiesItem;
            }
        }

        if (mpConfig != null && maConfig != null) {
            if (mpConfig.isExclusive() != maConfig.isExclusive()) {
                throw new WxConfigurationException("存在相同的小程序AppId以及公众号AppId两者排他登录配置不同!");
            }
        }

        if (mpConfig == null && maConfig == null) {
            throw new WxConfigurationException("不存在该AppId的排他登录!");
        }

        boolean exclusive = false;

        if (mpConfig != null) {
            exclusive = mpConfig.isExclusive();
        }

        if (maConfig != null) {
            exclusive = maConfig.isExclusive();
        }

        //排他登录，处理其他的Token
        if (exclusive) {
            //获取当前用户是否登录过一次
            boolean openIdSubscriberLogin = this.isOpenIdSubscriberLogin(appId, subscriberId);

            //删除当前用户
            if (openIdSubscriberLogin) {
                this.deleteOpenIdSubscriberCache(appId, subscriberId);
            }
        }

        //ID 对应 Token
        //Token 对应 用户信息
        //实现单点登录
        redisUtils.set(TOKEN_PREFIX, token);
        redisUtils.set(SUBSCRIBER_INFORMATION_PREFIX, umsSubscriber);
        return true;
    }

    public boolean saveOpenIdSubscriberTokenAndInformationCacheExclusive(String appId, Long subscriberId, String token, UmsSubscriberPO umsSubscriber) {
        final String TOKEN_PREFIX = subscriberRedisPrefixProperties.getToken() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + subscriberId;
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;

        //获取当前用户是否登录过一次
        boolean openIdSubscriberLogin = this.isOpenIdSubscriberLogin(appId, subscriberId);

        //删除当前用户
        if (openIdSubscriberLogin) {
            this.deleteOpenIdSubscriberCache(appId, subscriberId);
        }

        //ID 对应 Token
        //Token 对应 用户信息
        //实现单点登录
        redisUtils.set(TOKEN_PREFIX, token);
        redisUtils.set(SUBSCRIBER_INFORMATION_PREFIX, umsSubscriber);
        return true;
    }

    /**
     * 根据用户ID，用户登陆时Token删除用户缓存
     *
     * @param appId
     * @param subscriberId 用户ID
     * @param token        用户登录时Token
     * @return
     */
    public boolean deleteOpenIdSubscriberCache(String appId, Long subscriberId, String token) {
        final String TOKEN_PREFIX = subscriberRedisPrefixProperties.getToken() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + subscriberId;
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;

        //删除掉用户信息缓存
        redisUtils.del(TOKEN_PREFIX, SUBSCRIBER_INFORMATION_PREFIX);
        return true;
    }

    /**
     * 根据用户ID删除用户缓存
     *
     * @param appId
     * @param subscriberId 用户ID
     * @return
     */
    public boolean deleteOpenIdSubscriberCache(String appId, Long subscriberId) {
        String token = this.getOpenIdSubscriberTokenBySubscriberId(appId, subscriberId);
        return this.deleteOpenIdSubscriberCache(appId, subscriberId, token);
    }

    /**
     * 根据用户登陆时Token删除用户缓存
     *
     * @param appId
     * @param token 用户登陆时Token
     * @return
     */
    public boolean deleteOpenIdSubscriberCache(String appId, String token) {
        UmsSubscriberPO openIdSubscriberByToken = this.getOpenIdSubscriberByToken(appId, token);
        Long subscriberId = openIdSubscriberByToken.getId();
        return this.deleteOpenIdSubscriberCache(appId, subscriberId, token);
    }

    /**
     * 判断用户是否已经存在
     *
     * @param appId
     * @param subscriberId 用户ID
     * @return
     */
    public boolean isOpenIdSubscriberLogin(String appId, Long subscriberId) {
        String openIdSubscriberTokenBySubscriberId = this.getOpenIdSubscriberTokenBySubscriberId(appId, subscriberId);
        if (openIdSubscriberTokenBySubscriberId == null) {
            return false;
        }
        return true;
    }

    /**
     * 根据Token获取用户信息
     *
     * @param appId
     * @param token 用户登录时Token
     * @return
     */
    public UmsSubscriberPO getOpenIdSubscriberByToken(String appId, String token) {
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;
        return redisUtils.get(SUBSCRIBER_INFORMATION_PREFIX);
    }


    /**
     * 根据用户ID获取用户当前Token
     *
     * @param appId
     * @param subscriberId 用户Id
     * @return
     */
    public String getOpenIdSubscriberTokenBySubscriberId(String appId, Long subscriberId) {
        final String TOKEN_PREFIX = subscriberRedisPrefixProperties.getToken() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + subscriberId;
        return redisUtils.get(TOKEN_PREFIX);
    }

    /**
     * 根据用户Token更新用户信息
     *
     * @param appId           App Id
     * @param token           用户登录Token
     * @param umsSubscriberPO 用户信息
     * @return
     */
    public boolean updateOpenIdSubscriberInformationByToken(String appId, String token, UmsSubscriberPO umsSubscriberPO) {
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;
        return redisUtils.set(SUBSCRIBER_INFORMATION_PREFIX, umsSubscriberPO);
    }

    /**
     * 根据用户ID更新用户缓存数据
     *
     * @param appId           App Id
     * @param subscriberId    用户ID
     * @param umsSubscriberPO 用户信息
     * @return
     */
    public boolean updateOpenIdSubscriberInformationBySubscriberId(String appId, Long subscriberId, UmsSubscriberPO umsSubscriberPO) {
        final String TOKEN_PREFIX = subscriberRedisPrefixProperties.getToken() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + subscriberId;
        String token = redisUtils.get(TOKEN_PREFIX);
        final String SUBSCRIBER_INFORMATION_PREFIX = subscriberRedisPrefixProperties.getInformation() + RedisKeyAndValueMagicData.SPLIT + appId + RedisKeyAndValueMagicData.SPLIT + token;
        return redisUtils.set(SUBSCRIBER_INFORMATION_PREFIX, umsSubscriberPO);
    }

    /**
     * 根据Token获取用户ID
     *
     * @param appId
     * @param token 用户登陆时Token
     * @return
     */
    public Long getOpenIdSubscriberIdByToken(String appId, String token) {
        UmsSubscriberPO openIdSubscriberByToken = this.getOpenIdSubscriberByToken(appId, token);
        return openIdSubscriberByToken.getId();
    }


}

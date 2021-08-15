package top.getawaycar.user.common.authorization.wechat.service;

import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppUpdateTelephoneDTO;
import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppRegisterDTO;

/**
 * <p>Title: IUserService</p>
 * <p>Description: 用户注册接口</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
public interface ISubscriberService {

    /**
     * 小程序用户注册(返回登录Token)
     *
     * @param appId              小程序ID
     * @param miniAppRegisterDTO (wx.login() code JSCode;wx.getUserProfile() signature 签名,rawData 原始数据,encryptedData 加密数据,iv 向量)
     * @return 返回登录Token
     */
    String maUserRegisterWithToken(String appId, MiniAppRegisterDTO miniAppRegisterDTO);

    /**
     * 小程序用户注册(<bold>不<bold/>返回登录Token)
     *
     * @param appId              小程序ID
     * @param miniAppRegisterDTO (wx.login() code JSCode;wx.getUserProfile() signature 签名,rawData 原始数据,encryptedData 加密数据,iv 向量)
     */
    void maUserRegister(String appId, MiniAppRegisterDTO miniAppRegisterDTO);

    /**
     * 小程序登录
     *
     * @param appId 小程序ID
     * @param code  wx.login code
     * @return {@link java.lang.String} 用户登录Token
     */
    String maLogin(String appId, String code);

    /**
     * 更新用户电话(不更新Token中数据)
     *
     * @param appId                     App Id
     * @param subscriberId              用户ID
     * @param miniAppUpdateTelephoneDTO 更新电话DTO {@link MiniAppUpdateTelephoneDTO}
     */
    void maUpdateSubscriberTelephone(String appId, Long subscriberId, MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO);

    /**
     * 更新用户电话(更新Token中数据)
     *
     * @param appId                     App Id
     * @param subscriberId              用户ID
     * @param miniAppUpdateTelephoneDTO 更新电话DTO {@link MiniAppUpdateTelephoneDTO}
     */
    void maUpdateSubscriberTelephoneWithCache(String appId, Long subscriberId, MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO);


    /**
     * 公众用户注册(返回登录Token)
     *
     * @param appId 公众号ID
     * @param code  (scope 请选用snsapi_userinfo)
     * @return 返回登录Token
     */
    String mpUserRegisterWithToken(String appId, String code);

    /**
     * 小程序用户注册(<bold>不<bold/>返回登录Token)
     *
     * @param appId 公众号ID
     * @param code  (scope 请选用snsapi_userinfo)
     */
    void mpUserRegister(String appId, String code);

    /**
     * 公众号登录
     *  @param appId 公众号ID
     * @param code  (scope 请选用snsapi_base )
     * @return
     */
    String mpLogin(String appId, String code);


}

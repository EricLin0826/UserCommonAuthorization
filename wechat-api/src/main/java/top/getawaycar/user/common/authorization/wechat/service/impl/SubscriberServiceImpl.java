package top.getawaycar.user.common.authorization.wechat.service.impl;

import top.getawaycar.user.common.authorization.dao.UmsSubscriberDAO;
import top.getawaycar.user.common.authorization.domain.UmsSubscriberPO;
import top.getawaycar.user.common.authorization.wechat.configuration.ma.WxMiniAppConfiguration;
import top.getawaycar.user.common.authorization.pojo.constant.WechatRegisterTypeEnum;
import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppRegisterDTO;
import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppUpdateTelephoneDTO;
import top.getawaycar.user.common.authorization.wechat.pojo.exception.*;
import top.getawaycar.user.common.authorization.wechat.service.ISubscriberService;
import top.getawaycar.user.common.authorization.utils.QueryWrapperUtils;
import top.getawaycar.user.common.authorization.wechat.utils.SubscriberRedisUtils;
import top.getawaycar.user.common.authorization.wechat.utils.TokenUtils;
import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.util.DesensitizedUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>Title: UserService</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/05
 */
@Service
@Slf4j
public class SubscriberServiceImpl implements ISubscriberService {

    private final UmsSubscriberDAO umsSubscriberDAO;

    public SubscriberServiceImpl(SubscriberRedisUtils subscriberRedisUtils, UmsSubscriberDAO umsSubscriberDAO, TokenUtils tokenUtils, WxMpService wxService) {
        this.subscriberRedisUtils = subscriberRedisUtils;
        this.umsSubscriberDAO = umsSubscriberDAO;
        this.tokenUtils = tokenUtils;
        this.wxService = wxService;
    }

    /**
     * ?????????????????????(???????????????)
     *
     * @param appId              ?????????ID
     * @param miniAppRegisterDTO (wx.login() code JSCode;wx.getUserProfile() signature ??????,rawData ????????????,encryptedData ????????????,iv ??????)
     * @return ????????????????????????ID
     */
    public UmsSubscriberPO maUserRegisterDatabaseOperation(String appId, MiniAppRegisterDTO miniAppRegisterDTO) {
        String code = miniAppRegisterDTO.getCode();
        String iv = miniAppRegisterDTO.getIv();
        String encryptedData = miniAppRegisterDTO.getEncryptedData();
        String rawData = miniAppRegisterDTO.getRawData();
        String signature = miniAppRegisterDTO.getSignature();

        final WxMaService wxService = WxMiniAppConfiguration.getMaService(appId);
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ThirdPartyAPIException("??????SessionKey???OpenId??????!");
        }

        String openid = sessionInfo.getOpenid();
        String unionId = sessionInfo.getUnionid();

        WxMaUserInfo userInfo = null;
        log.debug(sessionInfo.toString());
        if (!wxService.getUserService().checkUserInfo(sessionInfo.getSessionKey(), rawData, signature)) {
            throw new ThirdPartyAPIException("????????????????????????!");
        }
        userInfo = wxService.getUserService().getUserInfo(sessionInfo.getSessionKey(), encryptedData, iv);
        log.debug(userInfo.toString());


        String avatarUrl = userInfo.getAvatarUrl();
        String city = userInfo.getCity();
        String country = userInfo.getCountry();
        String gender = userInfo.getGender();
        String language = userInfo.getLanguage();
        String province = userInfo.getProvince();
        String nickName = userInfo.getNickName();

        //????????????????????????????????? EricLin
        UmsSubscriberPO umsSubscriberPO = null;
        //??????????????????
        if (StringUtils.hasText(openid)) {
            //OpenId??????
            umsSubscriberPO = this.getUmsSubscriberPOByOpenId(appId, openid);
            if (umsSubscriberPO == null) {
                umsSubscriberPO = this.getUmsSubscriberPOByUnionId(appId, unionId);
            }
        }

        if (umsSubscriberPO != null && !StringUtils.hasText(unionId)) {
            throw new RegisterException("???????????????!");
        }
        if (umsSubscriberPO != null && StringUtils.hasText(unionId)) {
            umsSubscriberPO.setWechatUnionId(unionId);
            umsSubscriberDAO.updateById(umsSubscriberPO);
            throw new RegisterException("???????????????!");
        }

        //????????????
        umsSubscriberPO = new UmsSubscriberPO();
        umsSubscriberPO.setWechatUnionId(unionId);
        umsSubscriberPO.setWechatOpenId(openid);
        umsSubscriberPO.setWechatAppId(appId);
        umsSubscriberPO.setWechatAvatarUrl(avatarUrl);
        umsSubscriberPO.setWechatCity(city);
        umsSubscriberPO.setWechatProvince(province);
        umsSubscriberPO.setWechatCountry(country);
        umsSubscriberPO.setWechatGender(gender);
        umsSubscriberPO.setWechatLanguage(language);
        umsSubscriberPO.setWechatNickName(nickName);
        umsSubscriberPO.setWechatType(WechatRegisterTypeEnum.MINI_APP.getCode());


        //????????????
        umsSubscriberDAO.insert(umsSubscriberPO);
        return umsSubscriberPO;
    }


    /**
     * ??????AppId???UnionId????????????
     *
     * @param appId
     * @param unionId
     * @return
     */
    private UmsSubscriberPO getUmsSubscriberPOByUnionId(String appId, String unionId) {
        QueryWrapper<UmsSubscriberPO> queryWrapper = QueryWrapperUtils.getQueryWrapper();
        queryWrapper.eq("wechat_app_id", appId);
        queryWrapper.eq("wechat_union_id", unionId);
        return umsSubscriberDAO.selectOne(queryWrapper);
    }

    /**
     * ??????AppId???OpenId????????????
     *
     * @param appId
     * @param openId
     * @return
     */
    private UmsSubscriberPO getUmsSubscriberPOByOpenId(String appId, String openId) {
        QueryWrapper<UmsSubscriberPO> queryWrapper = QueryWrapperUtils.getQueryWrapper();
        queryWrapper.eq("wechat_app_id", appId);
        queryWrapper.eq("wechat_open_id", openId);
        return umsSubscriberDAO.selectOne(queryWrapper);
    }


    private final SubscriberRedisUtils subscriberRedisUtils;

    private final TokenUtils tokenUtils;

    @Override
    public String maUserRegisterWithToken(String appId, MiniAppRegisterDTO miniAppRegisterDTO) {
        UmsSubscriberPO subscriber = this.maUserRegisterDatabaseOperation(appId, miniAppRegisterDTO);
        //??????Token
        String token = tokenUtils.getToken();
        subscriberRedisUtils.saveOpenIdSubscriberTokenAndInformationCache(appId, subscriber.getId(), token, subscriber);
        return token;
    }

    @Override
    public void maUserRegister(String appId, MiniAppRegisterDTO miniAppRegisterDTO) {
        this.maUserRegisterDatabaseOperation(appId, miniAppRegisterDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String maLogin(String appId, String code) {
        final WxMaService wxService = WxMiniAppConfiguration.getMaService(appId);
        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new ThirdPartyAPIException("??????SessionKey???OpenId??????!");
        }
        String openId = sessionInfo.getOpenid();
        String unionId = sessionInfo.getUnionid();

        UmsSubscriberPO subscriber = this.getUmsSubscriberPOByOpenId(appId, openId);
        if (subscriber == null) {
            if (StringUtils.hasText(unionId)) {
                subscriber = this.getUmsSubscriberPOByUnionId(appId, unionId);
            }
        }

        if (subscriber == null) {
            throw new LoginException("???????????????!");
        }

        //??????Token
        String token = tokenUtils.getToken();
        subscriberRedisUtils.saveOpenIdSubscriberTokenAndInformationCache(appId, subscriber.getId(), token, subscriber);

        subscriber.setLastLoginTime(System.currentTimeMillis());
        umsSubscriberDAO.updateById(subscriber);

        return token;
    }

    /**
     * ??????????????????
     *
     * @param appId                     App Id
     * @param miniAppUpdateTelephoneDTO ????????????
     * @return
     */
    public String maGetTelephoneInformation(String appId, MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO) {
        final WxMaService wxService = WxMiniAppConfiguration.getMaService(appId);
        String code = miniAppUpdateTelephoneDTO.getCode();
        String iv = miniAppUpdateTelephoneDTO.getIv();
        String encryptedData = miniAppUpdateTelephoneDTO.getEncryptedData();

        WxMaJscode2SessionResult sessionInfo = null;
        try {
            sessionInfo = wxService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new SubscriberInformationUpdateException("??????SessionKey???OpenId??????!");
        }

        String sessionKey = sessionInfo.getSessionKey();

        // ??????
        WxMaPhoneNumberInfo phoneNoInfo = null;
        try {
            phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        } catch (Exception exception) {
            exception.printStackTrace();
            log.error(exception.getMessage());
            throw new SubscriberInformationUpdateException("????????????????????????,?????????!");
        }
        return phoneNoInfo.getPhoneNumber();
    }

    @Override
    public void maUpdateSubscriberTelephone(String appId, Long subscriberId, MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO) {
        String telephone = this.maGetTelephoneInformation(appId, miniAppUpdateTelephoneDTO);
        //??????????????????
        QueryWrapper<UmsSubscriberPO> subscriberQueryWrapper = QueryWrapperUtils.getQueryWrapper(subscriberId);
        UmsSubscriberPO subscriberPO = umsSubscriberDAO.selectOne(subscriberQueryWrapper);
        subscriberPO.setWechatTelephone(telephone);
        //???????????? ????????????????????? '138****9130'
        //??????Hutool???????????????
        String desensitizationTelephone = DesensitizedUtil.mobilePhone(telephone);
        subscriberPO.setWechatDesensitizationTelephone(desensitizationTelephone);
        //???????????????????????????
        umsSubscriberDAO.updateById(subscriberPO);
    }

    @Override
    public void maUpdateSubscriberTelephoneWithCache(String appId, Long subscriberId, MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO) {
        String telephone = this.maGetTelephoneInformation(appId, miniAppUpdateTelephoneDTO);
        //??????????????????
        QueryWrapper<UmsSubscriberPO> subscriberQueryWrapper = QueryWrapperUtils.getQueryWrapper(subscriberId);
        UmsSubscriberPO subscriberPO = umsSubscriberDAO.selectOne(subscriberQueryWrapper);
        subscriberPO.setWechatTelephone(telephone);
        //???????????? ????????????????????? '138****9130'
        //??????Hutool???????????????
        String desensitizationTelephone = DesensitizedUtil.mobilePhone(telephone);
        subscriberPO.setWechatDesensitizationTelephone(desensitizationTelephone);
        //???????????????????????????
        umsSubscriberDAO.updateById(subscriberPO);
        //????????????
        subscriberRedisUtils.updateOpenIdSubscriberInformationBySubscriberId(appId, subscriberId, subscriberPO);
    }

    private final WxMpService wxService;

    /**
     * ???????????????
     *
     * @param appId AppId
     * @param code  code  (scope ?????????snsapi_userinfo)
     * @return
     */
    public UmsSubscriberPO mpUserRegisterDatabaseOperation(String appId, String code) {
        if (!this.wxService.switchover(appId)) {
            throw new WxConfigurationException(String.format("???????????????appid=[%s]????????????????????????", appId));
        }
        WxOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RegisterException("??????AccessToken??????!");
        }
        WxOAuth2UserInfo user = null;
        try {
            user = wxService.getOAuth2Service().getUserInfo(accessToken, null);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new RegisterException("????????????????????????!");
        }


        String avatarUrl = user.getHeadImgUrl();
        String city = user.getCity();
        String country = user.getCountry();
        Integer gender = user.getSex();
        String province = user.getProvince();
        String nickName = user.getNickname();

        String openId = user.getOpenid();
        String unionId = user.getUnionId();
        UmsSubscriberPO subscriber = this.getUmsSubscriberPOByOpenId(appId, openId);
        if (subscriber == null) {
            subscriber = this.getUmsSubscriberPOByUnionId(appId, unionId);
        }
        if (subscriber != null && StringUtils.hasText(unionId)) {
            subscriber.setWechatUnionId(unionId);
            umsSubscriberDAO.updateById(subscriber);
            throw new RegisterException("???????????????!");
        }
        if (subscriber != null && !StringUtils.hasText(unionId)) {
            throw new RegisterException("???????????????!");
        }

        subscriber = new UmsSubscriberPO();
        subscriber.setWechatType(WechatRegisterTypeEnum.MEDIA_PLATFORM.getCode());
        subscriber.setWechatGender(String.valueOf(gender));
        subscriber.setWechatNickName(nickName);
        subscriber.setWechatUnionId(unionId);
        subscriber.setWechatOpenId(openId);
        subscriber.setWechatAvatarUrl(avatarUrl);
        subscriber.setWechatCountry(country);
        subscriber.setWechatProvince(province);
        subscriber.setWechatCity(city);
        subscriber.setWechatAppId(appId);

        umsSubscriberDAO.insert(subscriber);
        return subscriber;
    }

    @Override
    public String mpUserRegisterWithToken(String appId, String code) {
        UmsSubscriberPO subscriber = this.mpUserRegisterDatabaseOperation(appId, code);
        //??????Token
        String token = tokenUtils.getToken();
        subscriberRedisUtils.saveOpenIdSubscriberTokenAndInformationCache(appId, subscriber.getId(), token, subscriber);
        return token;
    }

    @Override
    public void mpUserRegister(String appId, String code) {
        this.mpUserRegisterDatabaseOperation(appId, code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String mpLogin(String appId, String code) {
        if (!this.wxService.switchover(appId)) {
            throw new WxConfigurationException(String.format("???????????????appid=[%s]????????????????????????", appId));
        }
        WxOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new LoginException("??????AccessToken??????!");
        }

        String openId = accessToken.getOpenId();
        String unionId = accessToken.getUnionId();

        UmsSubscriberPO subscriber = this.getUmsSubscriberPOByOpenId(appId, openId);
        if (subscriber == null) {
            this.getUmsSubscriberPOByUnionId(appId, unionId);
        }

        if (subscriber == null) {
            throw new LoginException("??????????????????,???????????????!");
        }

        //??????Token
        String token = tokenUtils.getToken();
        subscriberRedisUtils.saveOpenIdSubscriberTokenAndInformationCache(appId, subscriber.getId(), token, subscriber);


        subscriber.setLastLoginTime(System.currentTimeMillis());
        umsSubscriberDAO.updateById(subscriber);

        return token;
    }
}

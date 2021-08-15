package top.getawaycar.user.common.authorization.wechat.controller.ma;

import top.getawaycar.user.common.authorization.wechat.pojo.dto.MiniAppUpdateTelephoneDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.getawaycar.user.common.authorization.wechat.pojo.annotation.CurrentSubscriberId;
import top.getawaycar.user.common.authorization.wechat.service.ISubscriberService;

/**
 * <p>Title: MpController</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/04
 */
@RestController
@RequestMapping("/wx/mini/app/user/")
@Slf4j
public class WxMiniAppUserController {


    private final ISubscriberService subscriberService;

    public WxMiniAppUserController(ISubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @PostMapping("/phone")
    public void phone(@RequestHeader("AppId") String appid, @CurrentSubscriberId Long subscriberId, @RequestBody MiniAppUpdateTelephoneDTO miniAppUpdateTelephoneDTO) {
        subscriberService.maUpdateSubscriberTelephoneWithCache(appid, subscriberId, miniAppUpdateTelephoneDTO);
    }

}

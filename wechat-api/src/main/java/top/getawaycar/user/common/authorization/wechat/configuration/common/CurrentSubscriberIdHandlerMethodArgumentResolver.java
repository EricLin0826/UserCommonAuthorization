package top.getawaycar.user.common.authorization.wechat.configuration.common;

import top.getawaycar.user.common.authorization.wechat.pojo.exception.UnauthorizedException;
import top.getawaycar.user.common.authorization.wechat.utils.SubscriberRedisUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import top.getawaycar.user.common.authorization.wechat.pojo.annotation.CurrentSubscriberId;

import javax.servlet.http.HttpServletRequest;

public class CurrentSubscriberIdHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final SubscriberRedisUtils subscriberRedisUtils;

    public CurrentSubscriberIdHandlerMethodArgumentResolver(SubscriberRedisUtils subscriberRedisUtils) {
        this.subscriberRedisUtils = subscriberRedisUtils;
    }


    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        if (methodParameter.hasParameterAnnotation(CurrentSubscriberId.class)) {
            return true;
        }
        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest httpServletRequest = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String token = httpServletRequest.getHeader("Authorization");
        String appId = httpServletRequest.getHeader("AppId");
        if (token == null) {
            throw new UnauthorizedException("用户尚未登陆,或登录已过期!");
        }
        Class<?> parameterType = methodParameter.getParameterType();
        if (parameterType.isAssignableFrom(Long.class)) {
            return subscriberRedisUtils.getOpenIdSubscriberIdByToken(appId, token);
        }

        return null;
    }
}

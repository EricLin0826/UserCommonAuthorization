package top.getawaycar.user.common.authorization.wms.configuration;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.getawaycar.user.common.authorization.pojo.exception.CommonException;
import top.getawaycar.user.common.authorization.pojo.vo.CommonResultVO;

/**
 * <p>Title: WmsRestControllerHandler</p>
 * <p>Description: 统一返回结果集(环绕处理)</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
@RestControllerAdvice
@Slf4j
public class WmsRestControllerHandler implements ResponseBodyAdvice {

    @ExceptionHandler(value = Exception.class)
    public Object exceptionHandler(Exception e) {
        CommonResultVO errorResponse = new CommonResultVO(false, 5000, "未知错误:服务器内部错误，请联系管理人员!", null);
        try {
            e.printStackTrace();
            log.error(e.getMessage());
            if (e instanceof CommonException) {
                CommonException erXianException = (CommonException) e;
                errorResponse = new CommonResultVO(false, erXianException.getCode(), e.getMessage(), null);
            } else if (e instanceof HttpRequestMethodNotSupportedException) {
                errorResponse = new CommonResultVO(false, 4002, "请求头错误:请求方式错误，请更换请求方式!", null);
            } else if (e instanceof HttpMediaTypeNotSupportedException) {
                errorResponse = new CommonResultVO(false, 4003, "请求头错误:媒体类型不支持!" +
                        ((HttpMediaTypeNotSupportedException) e).getContentType().toString(), null);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            errorResponse = new CommonResultVO(false, 5000, "未知错误:服务器内部错误，请联系管理人员!", null);
        }
        return errorResponse;
    }

    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, @NotNull MethodParameter methodParameter, @NotNull MediaType mediaType, @NotNull Class aClass, @NotNull ServerHttpRequest serverHttpRequest, @NotNull ServerHttpResponse serverHttpResponse) {
        if (o != null) {
            if (o instanceof CommonResultVO) {
                return o;
            } else if (o instanceof String) {
                return JSONUtil.toJsonStr(new CommonResultVO(true, 2000, "操作成功!", o));
            } else if (o instanceof ResponseEntity) {
                return o;
            } else {
                return new CommonResultVO(true, 2000, "操作成功!", o);
            }
        } else {
            return new CommonResultVO(true, 2000, "操作成功!", null);
        }
    }


}

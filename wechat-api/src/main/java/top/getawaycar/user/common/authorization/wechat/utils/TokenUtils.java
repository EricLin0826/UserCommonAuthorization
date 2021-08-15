package top.getawaycar.user.common.authorization.wechat.utils;

import top.getawaycar.user.common.authorization.wechat.configuration.common.SnowFlakeConfiguration;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * <p>Title: TokenUtils</p>
 * <p>Description: Token工具类</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
@Component
public class TokenUtils {


    private final SnowFlakeConfiguration snowFlakeConfiguration;

    public TokenUtils(SnowFlakeConfiguration snowFlakeConfiguration) {
        this.snowFlakeConfiguration = snowFlakeConfiguration;
    }

    public String getToken() {
        String token = snowFlakeConfiguration.snowflakeId() + StrUtil.EMPTY;
        String encryptForTheFirstTime = SecureUtil.md5().digestHex(token, StandardCharsets.UTF_8);
        String encryptForTheSecondTime = SecureUtil.md5().digestHex(encryptForTheFirstTime, StandardCharsets.UTF_8);
        return encryptForTheSecondTime;

    }

}

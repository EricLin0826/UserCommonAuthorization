package top.getawaycar.user.common.authorization.wechat.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>Title: MiniAppUpdateTelephoneDTO</p>
 * <p>Description: </p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniAppUpdateTelephoneDTO {

    /**
     * wx.login() 中的JSCode
     */
    private String code;
    /**
     * 签名
     */
    private String signature;
    /**
     * 未加密数据
     */
    private String rawData;
    /**
     * 加密数据
     */
    private String encryptedData;
    /**
     * 向量
     */
    private String iv;

}

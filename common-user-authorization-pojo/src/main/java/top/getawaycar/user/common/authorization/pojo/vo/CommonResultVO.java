package top.getawaycar.user.common.authorization.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>Title: CommonResultVO</p>
 * <p>Description: 统一返回结果</p>
 * <p>Company: <a href="www.getawaycar.top">www.getawaycar.top</a></p>
 *
 * @author EricLin
 * @date 2021/08/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommonResultVO {

    //是否成功
    private boolean success;
    //状态码
    private Integer code;
    //提示信息
    private String msg;
    //数据
    private Object data;


}

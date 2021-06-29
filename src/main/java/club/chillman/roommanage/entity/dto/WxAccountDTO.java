package club.chillman.roommanage.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author NIU
 * @createTime 2021/3/31 20:01
 */
@Data
@AllArgsConstructor
@ApiModel(value = "微信登录id+key", description = "微信登录id+key对象封装")
public class WxAccountDTO {
    @ApiModelProperty(value = "微信openid")
    private String wxOpenid;
    @ApiModelProperty(value = "sessionKey")
    private String sessionKey;
    @ApiModelProperty(value = "sessionKey")
    private String userId;
}

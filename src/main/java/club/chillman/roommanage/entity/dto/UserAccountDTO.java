package club.chillman.roommanage.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @author NIU
 * @createTime 2021/4/1 12:37
 */
@Data
public class UserAccountDTO {
    @NotBlank(message = "缺少参数id或id不合法")
    private String id;
    @NotBlank(message = "缺少参数password或password不合法")
    private String password;
    @NotBlank(message = "缺少参数code或code不合法")
    private String code;
}

package club.chillman.roommanage.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author NIU
 * @createTime 2021/5/6 2:30
 */
@Data
public class RegisterDTO {

    @NotBlank(message = "缺少参数id或id不合法")
    private String id;
    @NotBlank(message = "缺少参数password或password不合法")
    private String password;
    @NotBlank(message = "缺少参数code或code不合法")
    private String code;
    @NotBlank(message = "缺少参数name或name不合法")
    private String name;
    @NotNull(message = "缺少参数identity或identity不合法")
    @ApiModelProperty(value = "学生（0）/教师（1）/管理员（2）",example = "0")
    private Integer identity;

}

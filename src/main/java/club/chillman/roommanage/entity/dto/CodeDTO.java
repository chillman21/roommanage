package club.chillman.roommanage.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by EalenXie on 2019/4/25 15:29.
 */
@Data
public class CodeDTO {
    @NotBlank(message = "缺少参数code或code不合法")
    private String code;
}

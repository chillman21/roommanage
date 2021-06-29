package club.chillman.roommanage.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author NIU
 * @createTime 2021/5/6 4:01
 */
@Data
public class PasswordDTO {
    @NotBlank(message = "缺少参数password或password不合法")
    private String password;
    @NotBlank(message = "缺少参数newPassword或newPassword不合法")
    private String newPassword;
}

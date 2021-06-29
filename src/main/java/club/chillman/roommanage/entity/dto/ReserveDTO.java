package club.chillman.roommanage.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/4/17 14:15
 */
@Data
public class ReserveDTO {

    @ApiModelProperty(value = "会议室ID")
    private String roomId;

    @ApiModelProperty(value = "预订开始时间")
    private String startTime;

    @ApiModelProperty(value = "预订结束时间")
    private String endTime;
}

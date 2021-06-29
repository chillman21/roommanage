package club.chillman.roommanage.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NIU
 * @createTime 2021/4/11 2:51
 */
@ApiModel(value = "Room查询对象", description = "会议室查询对象封装")
@Data
public class RoomQuery implements Serializable {
    private static final long serialVersionUID = -2627890251504245380L;
    @ApiModelProperty(value = "全名")
    private String fullName;

    @ApiModelProperty(value = "楼号")
    private String building;

    @ApiModelProperty(value = "方位")
    private String position;

    @ApiModelProperty(value = "楼层")
    private String floor;

    @ApiModelProperty(value = "房间号")
    private String roomNo;
}

package club.chillman.roommanage.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/4/21 21:38
 */
@Data
public class OrderRecordVO implements Serializable {
    private static final long serialVersionUID = 3531845992013695724L;
    @ApiModelProperty(value = "预订订单号")
    private String id;

    @ApiModelProperty(value = "会议室ID")
    private String roomId;

    @ApiModelProperty(value = "全名")
    private String fullName;

    @ApiModelProperty(value = "预订开始时间")
    private Date startTime;

    @ApiModelProperty(value = "预订结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDeleted;

}

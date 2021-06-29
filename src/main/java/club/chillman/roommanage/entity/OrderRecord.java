package club.chillman.roommanage.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/4/10 20:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_record")
@ApiModel(value="预订记录", description="预订记录表")
public class OrderRecord implements Serializable {
    private static final long serialVersionUID = 3598994027938582595L;

    @ApiModelProperty(value = "预订订单号")
    private String id;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "会议室ID")
    private String roomId;

    @ApiModelProperty(value = "预订开始时间")
    private Date startTime;

    @ApiModelProperty(value = "预订结束时间")
    private Date endTime;


    @ApiModelProperty(value = "排序",example = "0")
    private Integer sort;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;


}
/**
 * CREATE TABLE `order_record` (
 *   `id` char(19) NOT NULL COMMENT '预订订单号',
 *   `user_id` char(19) NOT NULL COMMENT '用户学/工号',
 *   `room_id` char(19) NOT NULL COMMENT '会议室ID',
 *   `start_time` datetime NOT NULL COMMENT '预订开始时间',
 *   `end_time` datetime NOT NULL COMMENT '预订结束时间',
 *   `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
 *   `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
 *   `gmt_create` datetime NOT NULL COMMENT '创建时间',
 *   `gmt_modified` datetime NOT NULL COMMENT '更新时间',
 *   PRIMARY KEY (`id`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预订记录表';
 */

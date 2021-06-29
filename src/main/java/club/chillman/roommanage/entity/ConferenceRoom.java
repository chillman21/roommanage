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
 * @createTime 2021/4/9 22:11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("conference_room")
@ApiModel(value="会议室房间表", description="预订记录表")
public class ConferenceRoom implements Serializable {
    private static final long serialVersionUID = 6584964755824794989L;
    @ApiModelProperty(value = "会议室id")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

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

    @ApiModelProperty(value = "容量",example = "0")
    private Integer capacity;

    @ApiModelProperty(value = "该会议室是否有多媒体功能，1为有")
    private String multimedia;

    @ApiModelProperty(value = "备注")
    private String remarks;

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
 * CREATE TABLE `conference_room` (
 *   `id` char(19) NOT NULL COMMENT '会议室id',
 *   `full_name` varchar(20) NOT NULL COMMENT '全名',
 *   `building` varchar(20) NOT NULL COMMENT '楼号',
 *   `position` varchar(4) NOT NULL COMMENT '方位',
 *   `floor` varchar(4) NOT NULL COMMENT '楼层',
 *   `room_no` varchar(20) NOT NULL COMMENT '房间号',
 *   `capacity` int(10) NOT NULL DEFAULT '20' COMMENT '会议室人数容量',
 *   `multimedia` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '该会议室是否有多媒体功能，1为有',
 *   `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
 *   `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
 *   `gmt_create` datetime NOT NULL COMMENT '创建时间',
 *   `gmt_modified` datetime NOT NULL COMMENT '更新时间',
 *   PRIMARY KEY (`id`),
 *   UNIQUE KEY `full_name` (`full_name`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会议室';
 */

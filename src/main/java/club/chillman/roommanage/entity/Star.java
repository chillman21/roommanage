package club.chillman.roommanage.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author NIU
 * @createTime 2021/5/5 20:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("star")
@ApiModel(value="收藏表", description="收藏表")
public class Star implements Serializable {
    private static final long serialVersionUID = -6452894696788470985L;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "会议室ID")
    private String roomId;


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
 * CREATE TABLE `star` (
 *   `user_id` char(19) NOT NULL COMMENT '用户学/工号',
 *   `room_id` char(19) NOT NULL COMMENT '会议室ID',
 *   `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
 *   `is_deleted` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '逻辑删除 1（true）已删除， 0（false）未删除',
 *   `gmt_create` datetime NOT NULL COMMENT '创建时间',
 *   `gmt_modified` datetime NOT NULL COMMENT '更新时间',
 *   PRIMARY KEY (`user_id`,`room_id`) USING BTREE
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPACT COMMENT='收藏表';
 *
 */

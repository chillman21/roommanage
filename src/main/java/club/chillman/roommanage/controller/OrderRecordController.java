package club.chillman.roommanage.controller;

import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.dto.ReserveDTO;
import club.chillman.roommanage.entity.dto.ReserveDTO2;
import club.chillman.roommanage.entity.vo.OrderRecordVO;
import club.chillman.roommanage.service.ConferenceRoomService;
import club.chillman.roommanage.service.OrderRecordService;
import club.chillman.roommanage.utils.JwtUtil;
import club.chillman.roommanage.utils.R;
import club.chillman.roommanage.utils.TimeUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 14:14
 */
@RestController
@RequestMapping("/api/record")
public class OrderRecordController {
    @Autowired
    private OrderRecordService orderRecordService;

    @ApiOperation("查询指定会议室从此刻往后7天内的预订记录")
    @RequiresAuthentication
    @GetMapping("/findRecords/{id}")
    public R findRecordsIn7Days(@ApiParam(name = "id",required = true) @PathVariable String id) {
        List<OrderRecord> records = orderRecordService.findRecordsIn7Days(id);
        return R.ok().data("list", records);
    }


    @ApiOperation("提交预订，检测是否与表内记录时间段冲突")
    @RequiresAuthentication
    @PostMapping("/reserveSubmit")
    public R reserveSubmit(@RequestBody ReserveDTO reserveDTO, @RequestHeader("Authorization") String token) {
        //预订时间一定是未来时间
        Date now = new Date();
        Date beforeDate = new Date(now .getTime() - 10*60*1000);
        if (!TimeUtil.str2Date(reserveDTO.getStartTime()).after(beforeDate)) return R.error().message("预订失败,只能预订未来时间");
        String userId = JwtUtil.getUserIdByToken(token);
        boolean flag = orderRecordService.reserveSubmit(reserveDTO, userId);
        if (flag) return R.ok().message("预订成功");
        else return R.error().message("预订失败,时间冲突");
    }

    @ApiOperation("修改预订，检测是否与表内记录时间段冲突")
    @RequiresAuthentication
    @PutMapping("/reserveModify/{id}")
    public R reserveModify(@RequestBody ReserveDTO reserveDTO,
                           @ApiParam(name = "recordId", value = "预订订单号", required = true)
                           @PathVariable("id") String  orderId) {
        //预订时间一定是未来时间
        Date now = new Date();
        Date beforeDate = new Date(now .getTime() - 10*60*1000);
        if (!TimeUtil.str2Date(reserveDTO.getStartTime()).after(beforeDate)) return R.error().message("预订失败,只能预订未来时间");
        boolean flag = orderRecordService.reserveModify(reserveDTO, orderId);
        if (flag) return R.ok().message("修改成功");
        else return R.error().message("修改失败,时间冲突");
    }

    @ApiOperation("取消预订")
    @RequiresAuthentication
    @DeleteMapping("/reserveCancel")
    public R reserveCancel(@RequestParam("id") String orderId, @RequestHeader("Authorization") String token) {
        OrderRecord orderRecord = new OrderRecord();
        orderRecord.setId(orderId).setIsDeleted(true);
        boolean flag = orderRecordService.updateById(orderRecord);
        if (flag) return R.ok().message("取消成功");
        else return R.error().message("取消失败");
    }

    @ApiOperation("查询指定用户的全部订单")
    @RequiresAuthentication
    @GetMapping("/findAllRecords/{current}/{limit}")
    public R findAllRecords(@ApiParam(name = "current", value = "当前页码", required = true)
                      @PathVariable("current") Long current,

                  @ApiParam(name = "limit", value = "每页记录数", required = true)
                      @PathVariable("limit") Long limit,
                  @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        IPage<OrderRecordVO> mapIPage = orderRecordService.findAllRecordsById(userId, current, limit);
        List<OrderRecordVO> records = mapIPage.getRecords();
        return R.ok().data("total", mapIPage.getTotal()).data("recordList", records);
    }

    @ApiOperation("查询指定用户的已完成订单")
    @RequiresAuthentication
    @GetMapping("/findCompletedRecords/{current}/{limit}")
    public R findCompletedRecords(@ApiParam(name = "current", value = "当前页码", required = true)
                            @PathVariable("current") Long current,

                            @ApiParam(name = "limit", value = "每页记录数", required = true)
                            @PathVariable("limit") Long limit,
                            @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        IPage<OrderRecordVO> mapIPage = orderRecordService.findCompletedRecordsById(userId, current, limit);
        List<OrderRecordVO> records = mapIPage.getRecords();
        return R.ok().data("total", mapIPage.getTotal()).data("recordList", records);
    }

    @ApiOperation("查询指定用户的预订订单，即还没有结束的订单")
    @RequiresAuthentication
    @GetMapping("/findReservedRecords/{current}/{limit}")
    public R findReservedRecords(@ApiParam(name = "current", value = "当前页码", required = true)
                                  @PathVariable("current") Long current,

                                  @ApiParam(name = "limit", value = "每页记录数", required = true)
                                  @PathVariable("limit") Long limit,
                                  @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        IPage<OrderRecordVO> mapIPage = orderRecordService.findReservedRecordsById(userId, current, limit);
        List<OrderRecordVO> records = mapIPage.getRecords();
        return R.ok().data("total", mapIPage.getTotal()).data("recordList", records);
    }


}

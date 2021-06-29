package club.chillman.roommanage.controller;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.OrderRecord;
import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.entity.dto.ReserveDTO;
import club.chillman.roommanage.service.ConferenceRoomService;
import club.chillman.roommanage.service.OrderRecordService;
import club.chillman.roommanage.service.UserAccountService;
import club.chillman.roommanage.utils.JwtUtil;
import club.chillman.roommanage.utils.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/5/4 19:18
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private ConferenceRoomService conferenceRoomService;
    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private OrderRecordService orderRecordService;

    @ApiOperation("查询所有房间")
    @RequiresAuthentication
    @GetMapping("/findRooms/{current}/{limit}")
    public R findRooms(@ApiParam(name = "current", value = "当前页码", required = true)
                           @PathVariable("current") Long current,

                       @ApiParam(name = "limit", value = "每页记录数", required = true)
                           @PathVariable("limit") Long limit,

                       @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            Page<ConferenceRoom> pageParam = new Page<ConferenceRoom>(current, limit);
            conferenceRoomService.page(pageParam, null);
            return R.ok().data("total", pageParam.getTotal()).data("list", pageParam.getRecords());
        } else return R.error();
    }

    @ApiOperation("查询所有预订记录")
    @RequiresAuthentication
    @GetMapping("/findRecords/{current}/{limit}")
    public R findRecords(@ApiParam(name = "current", value = "当前页码", required = true)
                             @PathVariable("current") Long current,

                         @ApiParam(name = "limit", value = "每页记录数", required = true)
                             @PathVariable("limit") Long limit,

                         @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            Page<OrderRecord> pageParam = new Page<OrderRecord>(current, limit);
            orderRecordService.page(pageParam, null);
            return R.ok().data("total", pageParam.getTotal()).data("list", pageParam.getRecords());
        } else return R.error();
    }

    @ApiOperation("查询所有用户信息")
    @RequiresAuthentication
    @GetMapping("/findUsers/{current}/{limit}")
    public R findUsers(@ApiParam(name = "current", value = "当前页码", required = true)
                           @PathVariable("current") Long current,

                       @ApiParam(name = "limit", value = "每页记录数", required = true)
                           @PathVariable("limit") Long limit,

                       @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            Page<UserAccount> pageParam = new Page<UserAccount>(current, limit);
            userAccountService.page(pageParam, null);
            return R.ok().data("total", pageParam.getTotal()).data("list", pageParam.getRecords());
        } else return R.error();
    }

    @ApiOperation("删除指定id的房间")
    @RequiresAuthentication
    @DeleteMapping("/deleteRoom/{id}")
    public R deleteRoom(@PathVariable("id") String  roomId,
                       @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = conferenceRoomService.removeById(roomId);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("删除指定id的账号")
    @RequiresAuthentication
    @DeleteMapping("/deleteUser/{id}")
    public R deleteUser(@PathVariable("id") String id,
                       @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = userAccountService.removeById(id);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("删除指定订单id的记录")
    @RequiresAuthentication
    @DeleteMapping("/deleteOrder/{id}")
    public R deleteOrder(@PathVariable("id") String  orderId,
                       @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = orderRecordService.removeById(orderId);
            if (flag) return R.ok();
        }
        return R.error();
    }


    @ApiOperation("管理员修改指定会议室信息")
    @RequiresAuthentication
    @PutMapping("/modifyRoom")
    public R modifyRoom(@RequestBody ConferenceRoom room,
                        @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = conferenceRoomService.updateById(room);
            if (flag) return R.ok();
        }
        return R.error();
    }


    @ApiOperation("管理员修改指定用户账户信息")
    @RequiresAuthentication
    @PutMapping("/modifyUser")
    public R modifyUser(@RequestBody UserAccount userAccount,
                        @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = userAccountService.updateById(userAccount);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("管理员修改指定预订记录")
    @RequiresAuthentication
    @PutMapping("/modifyRecord")
    public R modifyRecord(@RequestBody OrderRecord orderRecord,
                          @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = orderRecordService.updateById(orderRecord);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("管理员添加会议室")
    @RequiresAuthentication
    @PostMapping("/addRoom")
    public R addRoom(@RequestBody ConferenceRoom room,
                     @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = conferenceRoomService.save(room);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("管理员添加用户")
    @RequiresAuthentication
    @PostMapping("/addUser")
    public R addUser(@RequestBody UserAccount userAccount,
                        @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = userAccountService.save(userAccount);
            if (flag) return R.ok();
        }
        return R.error();
    }

    @ApiOperation("管理员添加记录")
    @RequiresAuthentication
    @PostMapping("/addRecord")
    public R addRecord(@RequestBody OrderRecord orderRecord,
                          @RequestHeader("Authorization") String token) {
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount user = userAccountService.getById(userId);
        if (!ObjectUtils.isEmpty(user) && user.getIdentity().equals(2)) {
            boolean flag = orderRecordService.save(orderRecord);
            if (flag) return R.ok();
        }
        return R.error();
    }
}

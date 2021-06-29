package club.chillman.roommanage.controller;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.dto.RoomQuery;
import club.chillman.roommanage.service.ConferenceRoomService;
import club.chillman.roommanage.utils.JwtUtil;
import club.chillman.roommanage.utils.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/4/11 1:57
 */
@RestController
@RequestMapping("/api/room")
public class RoomController {
    @Autowired
    private ConferenceRoomService conferenceRoomService;

    //@RequiresAuthentication
    @ApiOperation("查看所有会议室")
    @GetMapping("/findAll")
    public R findAll() {
        List<ConferenceRoom> list = conferenceRoomService.list(null);
        return R.ok().data("list", list);
    }

    @ApiOperation("分页带条件查询会议室")
    @PostMapping("/findRoom/{current}/{limit}")
    public R findRoomWithCondition(@ApiParam(name = "current", value = "当前页码", required = true)
                                   @PathVariable("current") Long current,

                                   @ApiParam(name = "limit", value = "每页记录数", required = true)
                                   @PathVariable("limit") Long limit,

                                   @ApiParam(name = "RoomQuery", value = "查询对象", required = false)
                                   @RequestBody(required = false) RoomQuery roomQuery) {
        System.out.println("findRoom:"+roomQuery);
        Page<ConferenceRoom> pageParam = new Page<ConferenceRoom>(current, limit);
        conferenceRoomService.pageQuery(pageParam, roomQuery);
        List<ConferenceRoom> records = pageParam.getRecords();
        return R.ok().data("total", pageParam.getTotal()).data("list", records);
    }

    @ApiOperation("查询当前空闲的会议室")
    @PostMapping("/findFreeRoom/{current}/{limit}")
    public R findFreeRoom(@ApiParam(name = "current", value = "当前页码", required = true)
                          @PathVariable("current") Long current,

                          @ApiParam(name = "limit", value = "每页记录数", required = true)
                          @PathVariable("limit") Long limit,

                          @ApiParam(name = "RoomQuery", value = "查询对象", required = false)
                          @RequestBody(required = false) RoomQuery roomQuery) {
        System.out.println("findFreeRoom:"+roomQuery);
        Page<ConferenceRoom> pageParam = new Page<ConferenceRoom>(current, limit);
        conferenceRoomService.findFreeRoom(pageParam, roomQuery);
        List<ConferenceRoom> records = pageParam.getRecords();
        return R.ok().data("total", pageParam.getTotal()).data("list", records);
    }

    @ApiOperation("获得指定ID的会议室信息")
    @RequiresAuthentication
    @GetMapping("/getRoom/{id}")
    public R getRoom(@ApiParam(name = "roomId", value = "会议室id", required = true)
                     @PathVariable("id") String  roomId,
                     @RequestHeader("Authorization") String token){
        ConferenceRoom room = conferenceRoomService.getById(roomId);

        return R.ok().data("room", room);
    }
}

package club.chillman.roommanage.controller;

import club.chillman.roommanage.entity.ConferenceRoom;
import club.chillman.roommanage.entity.Star;
import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.service.ConferenceRoomService;
import club.chillman.roommanage.service.StarService;
import club.chillman.roommanage.utils.JwtUtil;
import club.chillman.roommanage.utils.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author NIU
 * @createTime 2021/5/5 20:30
 */
@RestController
@RequestMapping("/api/star")
public class StarController {
    @Autowired
    private StarService starService;
    @Autowired
    private ConferenceRoomService conferenceRoomService;

    @ApiOperation("获得用户自己的收藏列表")
    @RequiresAuthentication
    @GetMapping("/getList")
    public R getList(@RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        List<ConferenceRoom> starList = conferenceRoomService.getStarList(userId);
        return R.ok().data("list", starList);
    }

    @ApiOperation("查看某房间是否已收藏")
    @RequiresAuthentication
    @GetMapping("/checkIfStared/{id}")
    public R checkIfStared(@PathVariable("id") String  roomId,
                           @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("room_id", roomId);
        Star star = starService.getOne(queryWrapper);
        if (!ObjectUtils.isEmpty(star)) return R.ok();
        return R.error();
    }

    @ApiOperation("提交收藏")
    @RequiresAuthentication
    @PutMapping("/submitStar/{id}")
    public R submitStar(@PathVariable("id") String  roomId,
                           @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        Star star = new Star();
        star.setUserId(userId);
        star.setRoomId(roomId);
        boolean flag = starService.save(star);
        if (flag) return R.ok();
        return R.error();
    }

    @ApiOperation("提交收藏")
    @RequiresAuthentication
    @DeleteMapping("/cancelStar/{id}")
    public R cancelStar(@PathVariable("id") String  roomId,
                        @RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        QueryWrapper<Star> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("room_id", roomId);
        boolean flag = starService.remove(queryWrapper);
        if (flag) return R.ok();
        return R.error();
    }
}

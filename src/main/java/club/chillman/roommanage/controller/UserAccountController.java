package club.chillman.roommanage.controller;

import club.chillman.roommanage.config.jwt.JwtConfig;
import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.entity.dto.PasswordDTO;
import club.chillman.roommanage.entity.dto.UserAccountDTO;
import club.chillman.roommanage.exception.RoomManageException;
import club.chillman.roommanage.service.UserAccountService;
import club.chillman.roommanage.service.WxAppletService;
import club.chillman.roommanage.utils.JwtUtil;
import club.chillman.roommanage.utils.R;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @author NIU
 * @createTime 2021/4/1 12:20
 */
@RestController
@RequestMapping("/api/user")
public class UserAccountController {
    @Autowired
    private WxAppletService wxAppletService;
    @Autowired
    private UserAccountService userAccountService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private JwtConfig jwtConfig;
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R userLogin(@RequestBody @Validated @ApiParam(name = "UserAccountDTO", value = "查询对象", required = false)
                                   UserAccountDTO userAccountDTO) {
        String wxOpenid = userAccountService.login(userAccountDTO);
        if (StringUtils.isEmpty(wxOpenid)) {
            throw new RoomManageException(666, "请绑定微信号!");
        }
        String token = wxAppletService.wxUserLogin(userAccountDTO.getCode());
        //String wxOpenId = JWT.decode(token).getClaim("wxOpenId").asString();
        return R.ok().data("token", token);
    }

    @ApiOperation("获得用户信息")
    @GetMapping("/getUser/{id}")
    public R getUser(@ApiParam(name = "id",required = true) @PathVariable String id) {
        UserAccount userAccount = userAccountService.getById(id);
        return R.ok().data("user", userAccount);
    }

    @ApiOperation("获得自己的信息")
    @RequiresAuthentication
    @GetMapping("/getUserOwnInfo")
    public R getUserOwnInfo(@RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount userAccount = userAccountService.getById(userId);
        return R.ok().data("user", userAccount);
    }

    @ApiOperation("注销登录")
    @DeleteMapping("/logout")
    public R logout(@RequestHeader("Authorization") String token) {
        if (StringUtils.isEmpty(token)) return R.ok();
        Boolean flag = stringRedisTemplate.delete("JWT-SESSION-" + JWT.decode(token).getClaim("jwt-id").asString());
        return flag ? R.ok() : R.error().message("未在登录态");
    }

    @ApiOperation("修改密码")
    @PutMapping("/modifyPassword")
    @RequiresAuthentication
    public R modifyPassword(@RequestHeader("Authorization") String token,
                            @RequestBody @Validated PasswordDTO passwordDTO) {
        if (StringUtils.isEmpty(token)) return R.error().message("登录态无效！");
        String userId = JwtUtil.getUserIdByToken(token);
        UserAccount userAccount = userAccountService.getById(userId);
        System.out.println(userAccount.getPassword() + "----" + passwordDTO.getPassword());
        if (!userAccount.getPassword().equals(passwordDTO.getPassword())) {
            return R.error().message("密码错误！");
        }
        //设置为新密码
        userAccount.setPassword(passwordDTO.getNewPassword());
        boolean flag = userAccountService.updateById(userAccount);
        if (flag) return R.ok().message("修改密码成功");
        return R.error().message("修改密码失败");
    }




}

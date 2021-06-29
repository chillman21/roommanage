package club.chillman.roommanage.controller;

import club.chillman.roommanage.entity.dto.CodeDTO;
import club.chillman.roommanage.entity.dto.PasswordDTO;
import club.chillman.roommanage.entity.dto.RegisterDTO;
import club.chillman.roommanage.entity.dto.UserAccountDTO;
import club.chillman.roommanage.service.UserAccountService;
import club.chillman.roommanage.service.WxAppletService;
import club.chillman.roommanage.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @author NIU
 * @createTime 2021/3/31 1:20
 */
@RestController
@RequestMapping("/api/wx")
public class WxAppletController {
    @Autowired
    private WxAppletService wxAppletService;

    @Autowired
    private UserAccountService userAccountService;
    @ApiOperation("用户微信一键登录")
    @PostMapping("/login")
    public R wxAppletLogin(@RequestBody @Validated CodeDTO codeDTO) {
        String token = wxAppletService.wxUserLogin(codeDTO.getCode());
        if (StringUtils.isEmpty(token)) return R.mid().message("未绑定学号，请先登录校内账号");

        return R.ok().data("token", token).message("登录成功");
    }
    //绑定微信号
    @ApiOperation("用户绑定微信")
    @PostMapping("/bind")
    public R wxBind(@RequestBody @Validated UserAccountDTO userAccountDTO) {
        userAccountService.login(userAccountDTO);
        String token = wxAppletService.wxUserBindAndLogin(userAccountDTO);
        return R.ok().data("token", token).message("绑定成功");
    }

    //用户注册
    @ApiOperation("用户注册")
    @PostMapping("/register")
    public R wxRegister(@RequestBody @Validated RegisterDTO registerDTO) {
        String token = wxAppletService.wxRegister(registerDTO);
        if (StringUtils.isEmpty(token)) return R.error().message("注册失败");
        return R.ok().data("token", token).message("注册成功");
    }


}

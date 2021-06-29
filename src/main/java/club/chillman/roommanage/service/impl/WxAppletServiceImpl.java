package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.config.jwt.JwtConfig;
import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.entity.dto.RegisterDTO;
import club.chillman.roommanage.entity.dto.TokenDTO;
import club.chillman.roommanage.entity.dto.UserAccountDTO;
import club.chillman.roommanage.entity.dto.WxAccountDTO;
import club.chillman.roommanage.entity.vo.Code2SessionResponse;
import club.chillman.roommanage.exception.RoomManageException;
import club.chillman.roommanage.service.UserAccountService;
import club.chillman.roommanage.service.WxAppletService;
import club.chillman.roommanage.utils.HttpUtil;
import club.chillman.roommanage.utils.JsonUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.net.URI;

/**
 * @author NIU
 * @createTime 2021/3/31 13:27
 */
@Service
public class WxAppletServiceImpl implements WxAppletService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${wx.applet.appid}")
    private String appid;

    @Value("${wx.applet.appsecret}")
    private String appSecret;

    @Autowired
    private UserAccountService userAccountService;

    @Resource
    private JwtConfig jwtConfig;

    /**
     * 微信的 code2session 接口 获取微信用户信息
     * 官方说明 : https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
     */
    private String code2Session(String jsCode) {
        String code2SessionUrl = "https://api.weixin.qq.com/sns/jscode2session";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("appid", appid);
        params.add("secret", appSecret);
        params.add("js_code", jsCode);
        params.add("grant_type", "authorization_code");
        URI code2Session = HttpUtil.getURIwithParams(code2SessionUrl, params);
        return restTemplate.exchange(code2Session, HttpMethod.GET, new HttpEntity<String>(new HttpHeaders()), String.class).getBody();
    }

    @Override
    public String wxUserLogin(String code) {
        //1 . code2session返回JSON数据
        String resultJson = code2Session(code);
        //2 . 解析数据
        Code2SessionResponse response = JsonUtil.toJavaObject(resultJson, Code2SessionResponse.class);
        if (!response.getErrcode().equals("0"))
            throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
        else {
            //3 . 先从本地数据库中查找用户是否存在
            String openid = response.getOpenid();
            QueryWrapper<UserAccount> wrapper = new QueryWrapper<>();
            wrapper.eq("wx_openid",openid);
            UserAccount userAccount = userAccountService.getOne(wrapper);
            System.out.println("-----" + userAccount.getName() + "微信登录");
            if (ObjectUtils.isEmpty(userAccount)) {
                return "";
            }
            //4 . 更新sessionKey和 登陆时间
            WxAccountDTO wxAccountDTO = new WxAccountDTO(openid,response.getSession_key(), userAccount.getId());
            //5 . JWT 返回自定义登陆态 Token
            String token = jwtConfig.createTokenByWxAccount(wxAccountDTO);
            System.out.println("-----" + userAccount.getName() + "微信登录成功");
            return token;
        }
    }

    @Override
    public String wxUserBindAndLogin(UserAccountDTO userAccountDTO) {
        //1 . code2session返回JSON数据
        String resultJson = code2Session(userAccountDTO.getCode());
        //2 . 解析数据
        Code2SessionResponse response = JsonUtil.toJavaObject(resultJson, Code2SessionResponse.class);
        if (!response.getErrcode().equals("0"))
            throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
        else {
            //3 . 更新openid
            UserAccount originalUserAccount = userAccountService.getById(userAccountDTO.getId());
            UpdateWrapper<UserAccount> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", originalUserAccount.getId());
            wrapper.set("wx_openid", response.getOpenid());
            System.out.println("-------" + response.getOpenid());
            userAccountService.update(originalUserAccount, wrapper);
            //4 . 更新sessionKey和 登陆时间
            WxAccountDTO wxAccountDTO = new WxAccountDTO(response.getOpenid(), response.getSession_key(), originalUserAccount.getId());
            //5 . JWT 返回自定义登陆态 Token
            return jwtConfig.createTokenByWxAccount(wxAccountDTO);
        }
    }

    @Override
    public String wxRegister(RegisterDTO registerDTO) {
        //1 . code2session返回JSON数据
        String resultJson = code2Session(registerDTO.getCode());
        //2 . 解析数据
        Code2SessionResponse response = JsonUtil.toJavaObject(resultJson, Code2SessionResponse.class);
        if (!response.getErrcode().equals("0"))
            throw new AuthenticationException("code2session失败 : " + response.getErrmsg());
        else {
            UserAccount userAccount = new UserAccount();
            userAccount.setId(registerDTO.getId())
                    .setWxOpenid(response.getOpenid())
                    .setPassword(registerDTO.getPassword())
                    .setName(registerDTO.getName())
                    .setIdentity(registerDTO.getIdentity());
            boolean saved = userAccountService.save(userAccount);
            WxAccountDTO wxAccountDTO = new WxAccountDTO(response.getOpenid(), response.getSession_key(), registerDTO.getId());
            // JWT 返回自定义登陆态 Token
            if (saved) return jwtConfig.createTokenByWxAccount(wxAccountDTO);
            return "";
        }
    }


}

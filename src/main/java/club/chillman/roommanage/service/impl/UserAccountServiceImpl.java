package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.entity.dto.UserAccountDTO;
import club.chillman.roommanage.exception.RoomManageException;
import club.chillman.roommanage.mapper.UserAccountMapper;
import club.chillman.roommanage.service.UserAccountService;
import club.chillman.roommanage.utils.R;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author NIU
 * @createTime 2021/4/1 4:00
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {
    /**
     *
     * @param userAccountDTO 账号对象
     * @return 若已绑定微信，返回微信openid
     */
    @Override
    public String login(UserAccountDTO userAccountDTO) {
        UserAccount userAccount = baseMapper.selectById(userAccountDTO.getId());
        if (userAccount == null) {
            throw new RoomManageException(404, "账号不存在");
        }
        if (!userAccountDTO.getPassword().equals(userAccount.getPassword())) {
            System.out.println("您输入的密码：" + userAccountDTO.getPassword());
            System.out.println("数据库中的密码：" + userAccount.getPassword());
            throw new RoomManageException(404, "密码错误");
        }
        System.out.println("*****" + userAccount.getName() + "登录成功");

        return userAccount.getWxOpenid();
    }
}

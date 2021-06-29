package club.chillman.roommanage.service;

import club.chillman.roommanage.entity.UserAccount;
import club.chillman.roommanage.entity.dto.TokenDTO;
import club.chillman.roommanage.entity.dto.UserAccountDTO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author NIU
 * @createTime 2021/4/1 3:10
 */
public interface UserAccountService extends IService<UserAccount> {
    String login(UserAccountDTO userAccountDTO);
}

package club.chillman.roommanage.service.impl;

import club.chillman.roommanage.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author NIU
 * @createTime 2021/3/30 21:00
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public String test() {
        return "hello niu";
    }
}

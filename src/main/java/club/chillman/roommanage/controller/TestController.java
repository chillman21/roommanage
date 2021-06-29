package club.chillman.roommanage.controller;

import club.chillman.roommanage.service.TestService;
import club.chillman.roommanage.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author NIU
 * @createTime 2021/3/30 20:58
 */
@RestController
@CrossOrigin
@RequestMapping("/api/test")
public class TestController {
    @Autowired
    private TestService testService;
    // 测试
    @GetMapping("/get")
    public R getTest() {
        String test = testService.test();
        return R.ok().data("testString",test);
    }
}

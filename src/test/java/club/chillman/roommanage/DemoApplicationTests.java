package club.chillman.roommanage;

import club.chillman.roommanage.utils.SpringBootRedisLock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author NIU
 * @createTime 2021/4/17 16:02
 */
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private SpringBootRedisLock springBootRedisLock;

    @Test
    public void testLock(){
        springBootRedisLock.lock("8017363909716979983282", 5L, 600L);
    }

    @Test
    public void testAgainLock(){
        springBootRedisLock.lock("8017363909716979983282", 5L, 600L);
    }

    @Test
    public void testUnlock(){
        springBootRedisLock.unlock("8017363909716979983282");
    }

    @Test
    public void testGet(){
        System.out.println(springBootRedisLock.query("8017363909716979983282"));
    }
}
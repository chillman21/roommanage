package club.chillman.roommanage.utils;

import club.chillman.roommanage.exception.RoomManageException;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * SpringBootRedisLock
 *
 * @author zhanghaiyan 2019/2/13
 * @description 使用spring整合的redis实现分布式锁
 * @modifier
 */
@Component
public class SpringBootRedisLock {

    /**
     * 加入spring-data-redis的依赖直接可以引用该类
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 默认最长等待时间
     */
    private static Long DEFAULT_MAX_WAIT_TIME = 5 * 1000L;

    /**
     * 默认锁最长持有时间
     */
    private static Long DEFAULT_MAX_DURATION_TIME = 2 * 60 * 1000L;

    /**
     * 加锁
     *
     * @param key
     * @param waitTime
     * @param durationTime
     */
    public void lock(String key, Long waitTime, Long durationTime) {
        Preconditions.checkArgument(StringUtils.isNotEmpty(key), "key为空");
        if (waitTime == null || waitTime > DEFAULT_MAX_WAIT_TIME) {
            waitTime = DEFAULT_MAX_WAIT_TIME;
        }
        if (durationTime == null || durationTime > DEFAULT_MAX_DURATION_TIME) {
            durationTime = DEFAULT_MAX_DURATION_TIME;
        }
        Long startLockTime = System.currentTimeMillis();
        try {
            //在等待时间之内轮询尝试获取锁
            while (true) {
                if ((System.currentTimeMillis() - startLockTime) / 1000 > waitTime) {
                    System.out.println("get lock timeout");
                    throw new RuntimeException("获取分布式锁超时");
                }
                Boolean result = tryLock(key, durationTime * 1000);
                if (result) {
                    //获取锁成功跳出循环
                    System.out.println("get lock success");
                    break;
                }
                try {
                    System.out.println("get lock waiting");
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("get lock sleep exception");
                    throw new RuntimeException("取锁线程中断异常");
                }
            }
        } catch (Exception e) {
            System.out.println("get lock exception");
            e.printStackTrace();
        }

    }

    /**
     * 释放锁
     *
     * @param key
     */
    public void unlock(String key) {
        Long lockExpireTime = Long.parseLong(redisTemplate.opsForValue().get(key).toString());
        if (System.currentTimeMillis() < lockExpireTime) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 获取key的value
     *
     * @param key
     * @return
     */
    public Long query(String key) {
        Long expireTime = Long.parseLong(redisTemplate.opsForValue().get(key).toString());
        return expireTime;
    }

    private Boolean tryLock(String key, Long durationTime) {
        //锁过期时间=当前系统时间+锁的持续时间,作为value
        Long lockExpireTime = System.currentTimeMillis() + durationTime + 1;
        Boolean result = redisTemplate.opsForValue().setIfAbsent(key, lockExpireTime.toString());
        if (result) {
            return true;
        }
        Long oldExpireTime = Long.parseLong(redisTemplate.opsForValue().get(key).toString());
        //锁已经过期，但没有释放
        if (System.currentTimeMillis() > oldExpireTime) {
            //给key设置新的value,同时获取上一次的value
            Long currentExpireTime = Long.parseLong(redisTemplate.opsForValue()
                    .getAndSet(key, lockExpireTime.toString()).toString());
            //对比value没有被别的线程修改，即可认为此次的锁获取成功
            if (currentExpireTime != null && currentExpireTime.equals(oldExpireTime)) {
                return true;
            }
        }
        return false;
    }
}
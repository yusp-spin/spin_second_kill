package com.spin.kill.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * redis集群分布式锁管理器，支持对单个资源加锁解锁，或给一批资源的批量加锁及解锁（用单机redis模拟集群看看有没有问题）
 */
@Component
public class RedisLockManger {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockManger.class);

    //设置3秒过期
    private static final int DEFAULT_SINGLE_EXPIRE_TIME = 3;

    //    private static final int DEFAULT_BATCH_EXPIRE_TIME = 6;

    //static的变量无法注解
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static RedisLockManger lockManger;

    public RedisLockManger() {
    }

    @PostConstruct
    private void init() {
        lockManger = this;
        lockManger.redisTemplate = this.redisTemplate;
    }
    /**
     * 获取锁 如果锁可用   立即返回true，  否则立即返回false，作为非阻塞式锁使用
     * @param key
     * @return
     */
    public boolean tryLock(String key , String value) {
        try {
            return tryLock(key, value, 0L, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false，作为阻塞式锁使用
     * @param key 锁键
     * @param value 被谁锁定
     * @param timeout 尝试获取锁时长，建议传递500,结合实践单位，则可表示500毫秒
     * @param unit，建议传递TimeUnit.MILLISECONDS
     * @return
     * @throws InterruptedException
     */
    public boolean tryLock(String key , String value , long timeout , TimeUnit unit) throws InterruptedException {
        //纳秒
        long begin = System.nanoTime();
        do {
            //LOGGER.debug("{}尝试获得{}的锁.", value, key);
            boolean i = lockManger.redisTemplate.opsForValue().setIfAbsent(key, value);
            if (i == true) {
                lockManger.redisTemplate.expire(key, DEFAULT_SINGLE_EXPIRE_TIME,TimeUnit.SECONDS);
                LOGGER.debug(value + "-成功获取{}的锁,设置锁过期时间为{}秒 ", key, DEFAULT_SINGLE_EXPIRE_TIME);
                return true;
            } else {
                // 存在锁 ，但可能获取不到，原因是获取的一刹那间
//                String desc = lockManger.jc.get(key);
//                LOGGER.error("{}正被{}锁定.", key, desc);
            } if (timeout == 0) {
                break;
            }
            //在其睡眠的期间，锁可能被解，也可能又被他人占用，但会尝试继续获取锁直到指定的时间
            Thread.sleep(100);
        } while ((System.nanoTime() - begin) < unit.toNanos(timeout));
        //因超时没有获得锁
        return false;
    }

    /**
     * 释放单个锁
     * @param key 锁键
     */
    public void unLock(String key) {
        lockManger.redisTemplate.delete(key);
        LOGGER.debug("{}锁被{}释放 .", key);
    }

    public String getValue(String key){
        String s = lockManger.redisTemplate.opsForValue().get(key);
        return s;
    }

}
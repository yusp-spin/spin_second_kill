package com.spin.kill.server.service.impl;

import com.spin.kill.server.entity.ItemKill;
import com.spin.kill.server.mapper.ItemKillMapper;
import com.spin.kill.server.mapper.ItemKillSuccessMapper;
import com.spin.kill.server.service.KillService;
import com.spin.kill.server.service.RabbitSenderService;
import com.spin.kill.server.service.SbKillService;
import com.spin.kill.server.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SbKillServiceImpl implements SbKillService {

    private static final Logger log= LoggerFactory.getLogger(KillService.class);

    private SnowFlake snowFlake=new SnowFlake(2,3);


    @Autowired
    private ItemKillMapper itemKillMapper;

    /**
     * 用for update实现悲观锁
     * @param killId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public Boolean killItems1(Integer killId, Integer userId) {
        Boolean result=false;
        //TODO:判断当前用户是否已经抢购过当前商品,测试高并发，一个用户可购买多次
        //TODO:查询待秒杀商品详情
        ItemKill itemKill = itemKillMapper.selectById2(killId);
        System.out.println(itemKill);
        //TODO:判断是否可以被秒杀canKill=1? 并且需要剩余数量大于0才可以秒杀
        if (itemKill != null && 1 == itemKill.getCanKill()&&itemKill.getTotal()>0) {
            //TODO:扣减库存-减一
            int res = itemKillMapper.updateKillItem2(killId);
            //TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
            if (res > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     * update锁表 悲观锁
     * @param killId
     * @param userId
     * @return
     */
    @Override
    public Boolean killItems2(Integer killId, Integer userId) {
        Boolean result=false;
        //TODO:判断当前用户是否已经抢购过当前商品,测试高并发，一个用户可购买多次
        //TODO:查询待秒杀商品详情
        ItemKill itemKill = itemKillMapper.selectById(killId);
        System.out.println(itemKill);
        //TODO:判断是否可以被秒杀canKill=1? 并且需要剩余数量大于0才可以秒杀
        if (itemKill != null && 1 == itemKill.getCanKill()&&itemKill.getTotal()>0) {
            //TODO:扣减库存-减一
            int res = itemKillMapper.updateKillItem3(killId);
            //TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
            if (res > 0) {
                result = true;
            }
        }
        return result;
    }

    /**
     *  乐观锁 version字段
     * @param killId
     * @param userId
     * @return
     */
    @Override
    public Boolean killItems3(Integer killId, Integer userId) {
        Boolean result=false;
        //TODO:判断当前用户是否已经抢购过当前商品,测试高并发，一个用户可购买多次
        //TODO:查询待秒杀商品详情
        ItemKill itemKill = itemKillMapper.selectById3(killId);
        System.out.println(itemKill);
        //TODO:判断是否可以被秒杀canKill=1? 并且需要剩余数量大于0才可以秒杀
        if (itemKill != null && 1 == itemKill.getCanKill()&&itemKill.getTotal()>0) {
            //TODO:扣减库存-减一
            int res = itemKillMapper.updateKillItem4(killId,itemKill.getVersion());
            //TODO:扣减是否成功?是-生成秒杀成功的订单，同时通知用户秒杀成功的消息
            if (res > 0) {
                result = true;
            }
        }
        return result;
    }
}

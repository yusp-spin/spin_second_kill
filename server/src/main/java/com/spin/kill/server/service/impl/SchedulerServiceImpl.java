package com.spin.kill.server.service.impl;

import com.spin.kill.server.entity.ItemKillSuccess;
import com.spin.kill.server.mapper.ItemKillSuccessMapper;
import com.spin.kill.server.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService {
    private static final Logger log= LoggerFactory.getLogger(SchedulerService.class);

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    @Autowired
    private Environment env;


    /**
     * 定时获取status=0的订单并判断是否超过TTL，然后进行失效
     * cron表达式
     */
//    @Scheduled(cron = "0/10 * * * * ? *")
    @Scheduled(cron = "0 0/30 * * * ?")
    public void schedulerExpireOrders(){
        //log.info("v1的定时任务----");

        try {
            List<ItemKillSuccess> list=itemKillSuccessMapper.selectExpireOrders();
            if (list!=null && !list.isEmpty()){
                //java8的写法
                list.stream().forEach(i -> {
                    if (i!=null && i.getDiffTime() > env.getProperty("scheduler.expire.orders.time",Integer.class)){
                        itemKillSuccessMapper.expireOrder(i.getCode());
                    }
                });
            }

            /*for (ItemKillSuccess entity:list){
            }*/ //非java8的写法
        }catch (Exception e){
            log.error("定时获取status=0的订单并判断是否超过TTL，然后进行失效-发生异常：",e.fillInStackTrace());
        }
    }

//    @Scheduled(cron = "0/11 * * * * ?")
//    public void schedulerExpireOrdersV2(){
//        log.info("v2的定时任务----");
//    }
//
//    @Scheduled(cron = "0/10 * * * * ?")
//    public void schedulerExpireOrdersV3(){
//        log.info("v3的定时任务----");
//    }
}



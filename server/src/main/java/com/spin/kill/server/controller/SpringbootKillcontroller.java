package com.spin.kill.server.controller;

import com.spin.kill.server.mapper.ItemKillSuccessMapper;
import com.spin.kill.server.service.SbKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用数据库乐观锁和悲观锁加锁
 */
@Controller
@RequestMapping("sbkill")
public class SpringbootKillcontroller {
    private static final Logger log = LoggerFactory.getLogger(SpringbootKillcontroller.class);

    @Autowired
    private SbKillService sbKillService;
    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    /***
     * 商品秒杀核心业务逻辑  数据库悲观锁 for update 加行锁实现
     * @param
     * @return
     * count为一次购买的数量
     */
    @RequestMapping("/execute/{killId}/{userId}")
    @ResponseBody
    public String execute(@PathVariable int killId, @PathVariable int userId, HttpSession session){
        if (killId<=0){
            return "商品不存在";
        }
        String result="<br><br><h3 align=\"center\" color=\"green\">恭喜你成功抢购该商品！！！！</h3>" +
                "<h4 align=\"center\">请您查看邮箱验证连接信息并在一小时内完成相关支付</h4>" +
                "<br><h4 align=\"center\"><a href=\"/kill/record/detail/"+killId+"/"+userId+"\">查看订单</a></h4>";
        try {
            //Boolean res=killService.killItem(dto.getKillId(),userId);
            Boolean res=sbKillService.killItems1(killId,userId);
            if (!res){
                return "哈哈~商品已抢购完毕或者不在抢购时间段哦!";
            }
        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            result="<br><br><h3 align=\"center\" color=\"red\">抢购失败啦</h3>";
        }
        return result;
    }


    /***
     * 商品秒杀核心业务逻辑  数据库悲观锁
     * @param
     * @return
     * count为一次购买的数量
     */
    @RequestMapping("/execute2/{killId}/{userId}")
    @ResponseBody
    public String execute2(@PathVariable int killId, @PathVariable int userId, HttpSession session){
        if (killId<=0){
            return "商品不存在";
        }
        String result="<br><br><h3 align=\"center\" color=\"green\">恭喜你成功抢购该商品！！！！</h3>" +
                "<h4 align=\"center\">请您查看邮箱验证连接信息并在一小时内完成相关支付</h4>" +
                "<br><h4 align=\"center\"><a href=\"/kill/record/detail/"+killId+"/"+userId+"\">查看订单</a></h4>";
        try {
            //Boolean res=killService.killItem(dto.getKillId(),userId);
            Boolean res=sbKillService.killItems2(killId,userId);
            if (!res){
                return "哈哈~商品已抢购完毕或者不在抢购时间段哦!";
            }
        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            result="<br><br><h3 align=\"center\" color=\"red\">抢购失败啦</h3>";
        }
        return result;
    }


    /***
     * 商品秒杀核心业务逻辑  数据库乐观锁
     * @param
     * @return
     * count为一次购买的数量
     */
    @RequestMapping("/execute3/{killId}/{userId}")
    @ResponseBody
    public String execute3(@PathVariable int killId, @PathVariable int userId, HttpSession session){
        if (killId<=0){
            return "商品不存在";
        }
        String result="<br><br><h3 align=\"center\" color=\"green\">恭喜你成功抢购该商品！！！！</h3>" +
                "<h4 align=\"center\">请您查看邮箱验证连接信息并在一小时内完成相关支付</h4>" +
                "<br><h4 align=\"center\"><a href=\"/kill/record/detail/"+killId+"/"+userId+"\">查看订单</a></h4>";
        try {
            Boolean res=sbKillService.killItems3(killId,userId);
            if (!res){
                return "哈哈~商品已抢购完毕或者不在抢购时间段哦!";
            }
        }catch (Exception e){
            result="<br><br><h3 align=\"center\" color=\"red\">抢购失败啦</h3>";
        }
        return result;
    }
}

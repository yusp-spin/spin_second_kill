package com.spin.kill.server.controller;/**
 * Created by Administrator on 2019/6/17.
 */

import com.spin.kill.api.enums.StatusCode;
import com.spin.kill.api.response.BaseResponse;
import com.spin.kill.server.dto.KillDto;
import com.spin.kill.server.dto.KillSuccessUserInfo;
import com.spin.kill.server.mapper.ItemKillSuccessMapper;

import com.spin.kill.server.service.KillService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * 秒杀controller
 * @Author:debug (SteadyJack)
 * @Date: 2019/6/17 22:14
 **/
@Controller
@RequestMapping("kill")
public class KillController {

    private static final Logger log = LoggerFactory.getLogger(KillController.class);


    @Autowired
    private KillService killService;

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    @RequestMapping("/aa")
    @ResponseBody
    public String test(){
        return "kill";
    }


    /***
     * 商品秒杀核心业务逻辑
     * @param
     * @return
     */
    @RequestMapping("/execute/{killId}/{userId}")
    @ResponseBody
    public String execute(@PathVariable int killId,@PathVariable int userId,  HttpSession session){
        System.out.println("aaa");
        if (killId<=0){
            return "商品不存在";
        }
//        Object uId=session.getAttribute("uid");
//        if (uId==null){
//            return new BaseResponse(StatusCode.UserNotLogin);
//        }
        //Integer userId=dto.getUserId();
//        Integer userId= (Integer)uId ;

//        BaseResponse response=new BaseResponse(StatusCode.Success);
        String result="<br><br><h3 align=\"center\" color=\"green\">恭喜你成功抢购该商品！！！！</h3>" +
                "<h4 align=\"center\">请您登陆邮箱验证连接信息并在一小时内完成相关支付</h4>";
        try {
            //Boolean res=killService.killItem(dto.getKillId(),userId);
            Boolean res=killService.killItem(killId,userId);
            if (!res){
                return "哈哈~商品已抢购完毕或者不在抢购时间段哦!";
            }
        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
            result="<br><br><h3 align=\"center\" color=\"red\">抢购失败啦</h3>";
        }
        return result;
    }







//
//    /***
//     * 商品秒杀核心业务逻辑-用于压力测试
//     * @param dto
//     * @param result
//     * @return
//     */
//    @RequestMapping(value = "/execute/lock",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public BaseResponse executeLock(@RequestBody @Validated KillDto dto, BindingResult result){
//        if (result.hasErrors() || dto.getKillId()<=0){
//            return new BaseResponse(StatusCode.InvalidParams);
//        }
//        BaseResponse response=new BaseResponse(StatusCode.Success);
//        try {
//            //不加分布式锁的前提
//            /*Boolean res=killService.killItemV2(dto.getKillId(),dto.getUserId());
//            if (!res){
//                return new BaseResponse(StatusCode.Fail.getCode(),"不加分布式锁-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
//            }*/
//
//            //基于Redis的分布式锁进行控制
//            /*Boolean res=killService.killItemV3(dto.getKillId(),dto.getUserId());
//            if (!res){
//                return new BaseResponse(StatusCode.Fail.getCode(),"基于Redis的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
//            }*/
//
//            //基于Redisson的分布式锁进行控制
//            /*Boolean res=killService.killItemV4(dto.getKillId(),dto.getUserId());
//            if (!res){
//                return new BaseResponse(StatusCode.Fail.getCode(),"基于Redisson的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
//            }*/
//
//            //基于ZooKeeper的分布式锁进行控制
//            Boolean res=killService.killItemV5(dto.getKillId(),dto.getUserId());
//            if (!res){
//                return new BaseResponse(StatusCode.Fail.getCode(),"基于ZooKeeper的分布式锁进行控制-哈哈~商品已抢购完毕或者不在抢购时间段哦!");
//            }
//
//        }catch (Exception e){
//            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
//        }
//        return response;
//    }
//
//
//
//
//
//
//
//
//
//    //http://localhost:8083/kill/kill/record/detail/343147116421722112
//
//    /**
//     * 查看订单详情
//     * @return
//     */
//    @RequestMapping(value = "/record/detail/{orderNo}",method = RequestMethod.GET)
//    public String killRecordDetail(@PathVariable String orderNo, ModelMap modelMap){
//        if (StringUtils.isBlank(orderNo)){
//            return "error";
//        }
//        KillSuccessUserInfo info=itemKillSuccessMapper.selectByCode(orderNo);
//        if (info==null){
//            return "error";
//        }
//        modelMap.put("info",info);
//        return "killRecord";
//    }
//
//
//    //抢购成功跳转页面
//    @RequestMapping(value = "/execute/success",method = RequestMethod.GET)
//    public String executeSuccess(){
//        return "executeSuccess";
//    }
//
//    //抢购失败跳转页面
//    @RequestMapping(value = "/execute/fail",method = RequestMethod.GET)
//    public String executeFail(){
//        return "executeFail";
//    }

}









































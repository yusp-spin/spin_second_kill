package com.spin.kill.server.controller;

import com.spin.kill.server.entity.ItemKill;
import com.spin.kill.server.service.ItemService;
import org.jboss.netty.channel.ExceptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 带秒杀商品
 */
@Controller
@RequestMapping("item")
public class ItemController {
    private static final Logger log= LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;
    @GetMapping
    public String list(ModelMap modelMap){
        try{
            List<ItemKill> itemKills=itemService.getKillItems();
            System.out.println(itemKills.get(0).getStartTime());
            modelMap.addAttribute("list",itemKills);
            log.info("获取带秒杀数据列表:()",itemKills);
        }catch (Exception e){
            log.error("获取待秒杀商品异常:",e.fillInStackTrace());
            return "redirect:base/error";
        }
        return "pages/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id,ModelMap modelMap){
        if(id==null||id<0){
            return "redirect:base/error";
        }
        try{
            ItemKill killDetail = itemService.getKillDetail(id);
            if(killDetail==null){
                throw  new Exception("获取详情异常");
            }
            modelMap.addAttribute("detail",killDetail);
        }catch (Exception e){
            log.error("获取带秒杀商品详情发生异常：id={}",id,e.fillInStackTrace());
        }
        return "pages/info";
    }
}

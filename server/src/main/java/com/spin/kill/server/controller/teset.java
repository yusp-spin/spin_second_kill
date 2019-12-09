package com.spin.kill.server.controller;

import com.spin.kill.api.enums.StatusCode;
import com.spin.kill.api.response.BaseResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class teset {

    @RequestMapping("/1")
    public String hello(){
        return "index";
    }

    @RequestMapping("/2")
    @ResponseBody
    public String hello2(){
        return "index";
    }

    @RequestMapping("/data")
    @ResponseBody
    public BaseResponse hello3(){
        return new BaseResponse(StatusCode.Success);
    }
}

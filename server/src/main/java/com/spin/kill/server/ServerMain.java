package com.spin.kill.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//两种启动方式
@SpringBootApplication
//@EnableTransactionManagement
//@MapperScan(basePackages = "com.spin.kill.server.mapper")
public class ServerMain extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ServerMain.class,args);
    }
}

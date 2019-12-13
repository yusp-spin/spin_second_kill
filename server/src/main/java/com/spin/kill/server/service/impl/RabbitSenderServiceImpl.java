package com.spin.kill.server.service.impl;

import com.spin.kill.server.dto.KillSuccessUserInfo;
import com.spin.kill.server.mapper.ItemKillSuccessMapper;
import com.spin.kill.server.service.RabbitSenderService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class RabbitSenderServiceImpl implements RabbitSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ItemKillSuccessMapper itemKillSuccessMapper;

    @Autowired
    private Environment env;

    public static final Logger log= LoggerFactory.getLogger(RabbitSenderServiceImpl.class);
    //秒杀成功异步发送邮件
    public void sendKillSuccessEmailMsg(String orderNo){
        log.info("秒杀成功 准备发送小时：{}",orderNo);
        try{
            if(StringUtils.isNoneBlank(orderNo)){
                KillSuccessUserInfo info=itemKillSuccessMapper.selectByCode(orderNo);
                if(info!=null){
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.email.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.email.routing.key"));
//                    Message message=MessageBuilder.withBody(orderNo.getBytes("UTF-8")).build();
//                    rabbitTemplate.convertAndSend(message);

                    //将info充当消息发送至队列
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties messageProperties=message.getMessageProperties();
                            messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_KEY_CLASSID_FIELD_NAME,KillSuccessUserInfo.class);
                            return message;
                        }
                    });
                }
            }

        }catch (Exception e){
            log.error("秒杀成功异步发送邮件通知消息-发送异常，消息为：{}",orderNo,e.fillInStackTrace());
        }
    }

    /**
     * 秒杀成功后生成抢购订单-发送信息入死信队列，等待着一定时间失效超时未支付的订单
     * @param orderCode
     */
    public void sendKillSuccessOrderExpireMsg(final String orderCode){
        try {
            if (StringUtils.isNotBlank(orderCode)){
                KillSuccessUserInfo info=itemKillSuccessMapper.selectByCode(orderCode);
                if (info!=null){
                    rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
                    rabbitTemplate.setExchange(env.getProperty("mq.kill.item.success.kill.dead.prod.exchange"));
                    rabbitTemplate.setRoutingKey(env.getProperty("mq.kill.item.success.kill.dead.prod.routing.key"));
                    rabbitTemplate.convertAndSend(info, new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            MessageProperties mp=message.getMessageProperties();
                            mp.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            mp.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,KillSuccessUserInfo.class);

                            //TODO：动态设置TTL(为了测试方便，暂且设置10s)
                            mp.setExpiration(env.getProperty("mq.kill.item.success.kill.expire"));
                            return message;
                        }
                    });
                }
            }
        }catch (Exception e){
            log.error("秒杀成功后生成抢购订单-发送信息入死信队列，等待着一定时间失效超时未支付的订单-发生异常，消息为：{}",orderCode,e.fillInStackTrace());
        }
    }

}



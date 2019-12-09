package com.spin.kill.server.service;

import com.spin.kill.server.dto.KillSuccessUserInfo;

public interface RabbitReceiveService {
    public void consumeEmailMsg(KillSuccessUserInfo info);
    public void consumeExpireOrder(KillSuccessUserInfo info);
}

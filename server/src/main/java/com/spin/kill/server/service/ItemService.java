package com.spin.kill.server.service;

import com.spin.kill.server.entity.ItemKill;

import java.util.List;

public interface ItemService {
    public  List<ItemKill> getKillItems() throws Exception;

    ItemKill getKillDetail(Integer id) throws  Exception;
}

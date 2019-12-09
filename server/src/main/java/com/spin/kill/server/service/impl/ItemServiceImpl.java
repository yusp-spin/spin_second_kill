package com.spin.kill.server.service.impl;

import com.spin.kill.server.entity.ItemKill;
import com.spin.kill.server.mapper.ItemKillMapper;
import com.spin.kill.server.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {
    private static final Logger log= LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemKillMapper itemKillMapper;
    public List<ItemKill> getKillItems() throws Exception{
        System.out.println("aaa");
        return itemKillMapper.selectAll();
    }

    @Override
    public ItemKill getKillDetail(Integer id) throws Exception {
        ItemKill itemKill = itemKillMapper.selectById(id);
        return itemKill;
    }
}

package com.spin.kill.server.mapper;

import com.spin.kill.server.entity.ItemKill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ItemKillMapper {
    @Select(" SELECT\n" +
            "      a.*,\n" +
            "      b.name AS itemName,\n" +
            "      (\n" +
            "        CASE WHEN (now() BETWEEN a.start_time AND a.end_time AND a.total > 0)\n" +
            "          THEN 1\n" +
            "        ELSE 0\n" +
            "        END\n" +
            "      )      AS canKill\n" +
            "    FROM item_kill AS a LEFT JOIN item AS b ON b.id = a.item_id\n" +
            "    WHERE a.is_active = 1")
    List<ItemKill> selectAll();


//    解决数据库与映射bean别名不一致问题
    @Select("    SELECT\n" +
            "      a.id,a.item_id as itemId,a.total,a.start_time as startTime,a.end_time as endTime,a.is_active as isActive,a.create_time as createTime,\n" +
            "      b.name AS itemName,\n" +
            "      (\n" +
            "        CASE WHEN (now() BETWEEN a.start_time AND a.end_time AND a.total > 0)\n" +
            "          THEN 1\n" +
            "        ELSE 0\n" +
            "        END\n" +
            "      )      AS canKill\n" +
            "    FROM item_kill AS a LEFT JOIN item AS b ON b.id = a.item_id\n" +
            "    WHERE a.is_active = 1 AND a.id= #{id}")
    ItemKill selectById(@Param("id") Integer id);

    @Update(" UPDATE item_kill\n" +
            "    SET total = total - 1\n" +
            "    WHERE\n" +
            "        id = #{killId}")
    int updateKillItem(@Param("killId") Integer killId);



    ItemKill selectByIdV2(@Param("id") Integer id);

    int updateKillItemV2(@Param("killId") Integer killId);
}
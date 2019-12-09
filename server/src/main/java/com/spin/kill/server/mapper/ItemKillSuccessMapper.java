package com.spin.kill.server.mapper;
import com.spin.kill.server.dto.KillSuccessUserInfo;
import com.spin.kill.server.entity.ItemKillSuccess;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ItemKillSuccessMapper {
    int deleteByPrimaryKey(String code);

    @Insert("insert into item_kill_success (code, item_id, kill_id, \n" +
            "      user_id, status, create_time\n" +
            "      )\n" +
            "    values (#{code,jdbcType=VARCHAR}, #{itemId,jdbcType=INTEGER}, #{killId,jdbcType=INTEGER}, \n" +
            "      #{userId,jdbcType=VARCHAR}, #{status,jdbcType=TINYINT}, #{createTime,jdbcType=TIMESTAMP}\n" +
            "      )")
    int insert(ItemKillSuccess record);

    @Insert("insert into item_kill_success\n" +
            "    <trim prefix="+"\"(\""+" suffix="+"\")\""+" suffixOverrides="+"\",\" >\n" +
            "      <if test=\"code != null\" >\n" +
            "        code,\n" +
            "      </if>\n" +
            "      <if test=\"itemId != null\" >\n" +
            "        item_id,\n" +
            "      </if>\n" +
            "      <if test=\"killId != null\" >\n" +
            "        kill_id,\n" +
            "      </if>\n" +
            "      <if test=\"userId != null\" >\n" +
            "        user_id,\n" +
            "      </if>\n" +
            "      <if test=\"status != null\" >\n" +
            "        status,\n" +
            "      </if>\n" +
            "      <if test=\"createTime != null\" >\n" +
            "        create_time,\n" +
            "      </if>\n" +
            "    </trim>\n" +
            "    <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >\n" +
            "      <if test=\"code != null\" >\n" +
            "        #{code,jdbcType=VARCHAR},\n" +
            "      </if>\n" +
            "      <if test=\"itemId != null\" >\n" +
            "        #{itemId,jdbcType=INTEGER},\n" +
            "      </if>\n" +
            "      <if test=\"killId != null\" >\n" +
            "        #{killId,jdbcType=INTEGER},\n" +
            "      </if>\n" +
            "      <if test=\"userId != null\" >\n" +
            "        #{userId,jdbcType=VARCHAR},\n" +
            "      </if>\n" +
            "      <if test=\"status != null\" >\n" +
            "        #{status,jdbcType=TINYINT},\n" +
            "      </if>\n" +
            "      <if test=\"createTime != null\" >\n" +
            "        #{createTime,jdbcType=TIMESTAMP},\n" +
            "      </if>\n" +
            "    </trim>")
    int insertSelective(ItemKillSuccess record);

    ItemKillSuccess selectByPrimaryKey(String code);

    int updateByPrimaryKeySelective(ItemKillSuccess record);

    int updateByPrimaryKey(ItemKillSuccess record);

    @Select("SELECT\n" +
            "        COUNT(1) AS total\n" +
            "    FROM\n" +
            "        item_kill_success\n" +
            "    WHERE\n" +
            "        user_id = #{userId}\n" +
            "    AND kill_id = #{killId}\n" +
            "    AND `status` IN (0)")
    int countByKillUserId(@Param("killId") Integer killId, @Param("userId") Integer userId);

    @Select("SELECT\n" +
            "      a.*,\n" +
            "      b.user_name,\n" +
            "      b.phone,\n" +
            "      b.email,\n" +
            "      c.name AS itemName\n" +
            "    FROM item_kill_success AS a\n" +
            "      LEFT JOIN user b ON b.id = a.user_id\n" +
            "      LEFT JOIN item c ON c.id = a.item_id\n" +
            "    WHERE a.code = #{code}\n" +
            "          AND b.is_active = 1")
    KillSuccessUserInfo selectByCode(@Param("code") String code);

    int expireOrder(@Param("code") String code);

    List<ItemKillSuccess> selectExpireOrders();
}
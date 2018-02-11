/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.service.impl;

import com.zjtachao.fish.goldfish.data.mapper.GoldFishUserRoomRelMapper;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserRoomRel;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserRoomRelRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserRoomRelSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishUserRoomRelService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 用户房间关联service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishUserRoomRelServiceImpl implements GoldFishUserRoomRelService {

    /** 用户房间关联mapper */
    @Resource
    private GoldFishUserRoomRelMapper goldFishUserRoomRelMapper;

    /**
     * 创建用户房间关联
     * @param userRoomRel
     */
    public void createUserRoomRel(GoldFishUserRoomRel userRoomRel){
        Date date = new Date();
        userRoomRel.setCreateTime(date);
        userRoomRel.setModifyTime(date);
        goldFishUserRoomRelMapper.createUserRoomRel(userRoomRel);
    }

    /**
     * 查询用户房间关联
     * @param userRoomRelSo
     */
    public List<GoldFishUserRoomRelRo> queryUserRoomRelList(GoldFishUserRoomRelSo userRoomRelSo){
        return goldFishUserRoomRelMapper.queryUserRoomRelList(userRoomRelSo);
    }

    /**
     * 删除用户房间关联
     * @param userRoomRel
     */
    public void deleteUserRoomRel(GoldFishUserRoomRel userRoomRel){
        userRoomRel.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        userRoomRel.setModifyTime(new Date());
        goldFishUserRoomRelMapper.deleteUserRoomRel(userRoomRel);
    }

    /**
     * 查询房间所有用户
     * @param userRoomRelSo
     */
    public List<GoldFishUserInfoRo> queryRoomUserList(GoldFishUserRoomRelSo userRoomRelSo){
        return goldFishUserRoomRelMapper.queryRoomUserList(userRoomRelSo);
    }

    /**
     * 查询房间所有用户数量
     * @param userRoomRelSo
     */
    public Long queryRoomUserCount(GoldFishUserRoomRelSo userRoomRelSo){
        return goldFishUserRoomRelMapper.queryRoomUserCount(userRoomRelSo);
    }
}

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

import com.zjtachao.fish.goldfish.data.mapper.GoldFishUserPrizeInfoMapper;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserPrizeInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserPrizeInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserPrizeInfoSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishUserPrizeInfoService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户中奖名单service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishUserPrizeInfoServiceImpl implements GoldFishUserPrizeInfoService {


    /** 用户中奖名单mapper */
    @Resource
    private GoldFishUserPrizeInfoMapper goldFishUserPrizeInfoMapper;

    /**
     * 查询用户中奖名单列表
     * @param userPrizeInfoSo
     * @return
     */
    public List<GoldFishUserPrizeInfoRo> queryUserPrizeInfoList(GoldFishUserPrizeInfoSo userPrizeInfoSo){
        return goldFishUserPrizeInfoMapper.queryUserPrizeInfoList(userPrizeInfoSo);
    }

    /**
     * 查询单个用户中奖名单
     * @param userPrizeInfoSo
     * @return
     */
    public GoldFishUserPrizeInfoRo querySingleUserPrizeInfo(GoldFishUserPrizeInfoSo userPrizeInfoSo){
        return goldFishUserPrizeInfoMapper.querySingleUserPrizeInfo(userPrizeInfoSo);
    }

    /**
     * 查询用户中奖名单数量
     * @param userPrizeInfoSo
     * @return
     */
    public Long queryUserPrizeInfoCount(GoldFishUserPrizeInfoSo userPrizeInfoSo){
        return goldFishUserPrizeInfoMapper.queryUserPrizeInfoCount(userPrizeInfoSo);
    }

    /**
     * 创建用户中奖名单
     * @param userPrizeInfo
     */
    public void createUserPrizeInfo(GoldFishUserPrizeInfo userPrizeInfo){
        Date date = new Date();
        userPrizeInfo.setCreateTime(date);
        userPrizeInfo.setModifyTime(date);
        goldFishUserPrizeInfoMapper.createUserPrizeInfo(userPrizeInfo);
    }


    /**
     * 删除用户中奖名单
     * @param userPrizeInfo
     */
    public void deleteUserPrizeInfo(GoldFishUserPrizeInfo userPrizeInfo){
        userPrizeInfo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        userPrizeInfo.setModifyTime(new Date());
        goldFishUserPrizeInfoMapper.deleteUserPrizeInfo(userPrizeInfo);
    }


    /**
     * 查询中奖名单 --用户列表
     * @param userPrizeInfoSo
     * @return
     */
    public List<GoldFishUserInfoRo> queryPrizeUserList(GoldFishUserPrizeInfoSo userPrizeInfoSo){
        return   goldFishUserPrizeInfoMapper.queryPrizeUserList(userPrizeInfoSo);
    }
}

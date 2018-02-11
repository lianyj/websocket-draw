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


import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.goldfish.data.mapper.GoldFishUserInfoMapper;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserInfoSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishUserInfoService;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.water.common.data.WaterRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户信息service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishUserInfoServiceImpl implements GoldFishUserInfoService {


    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    /** 用户信息mapper*/
    @Resource
    private GoldFishUserInfoMapper goldFishUserInfoMapper;

    /**
     * 查询用户列表
     * @param userInfoSo
     * @return
     */
    public List<GoldFishUserInfoRo> queryUserList(GoldFishUserInfoSo userInfoSo){
        return goldFishUserInfoMapper.queryUserList(userInfoSo);
    }

    /**
     * 查询单个用户
     * @param userInfoSo
     * @return
     */
    public GoldFishUserInfoRo querySingleUser(GoldFishUserInfoSo userInfoSo){
        return goldFishUserInfoMapper.querySingleUser(userInfoSo);
    }

    /**
     * 查询用户数量
     * @param userInfoSo
     * @return
     */
    public Long queryUserCount(GoldFishUserInfoSo userInfoSo){
        return goldFishUserInfoMapper.queryUserCount(userInfoSo);
    }

    /**
     * 创建用户
     * @param userInfo
     */
    public void createUser(GoldFishUserInfo userInfo){
        Date date = new Date();
        userInfo.setCreateTime(date);
        userInfo.setModifyTime(date);
        goldFishUserInfoMapper.createUser(userInfo);
    }

    /**
     * 修改用户
     * @param userInfo
     */
    public void updateUser(GoldFishUserInfo userInfo){
        Date date = new Date();
        userInfo.setModifyTime(date);
        goldFishUserInfoMapper.updateUser(userInfo);
    }

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    public GoldFishUserInfoRo getUserInfo(String token) {
        GoldFishUserInfoRo goldFishUserInfoRo = null;
        String userJson = waterRedis.queryString(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY + token);
        if (null != userJson && !"".equals(userJson)) {
            goldFishUserInfoRo = JSON.parseObject(userJson, GoldFishUserInfoRo.class);
        }
        return goldFishUserInfoRo;
    }

}

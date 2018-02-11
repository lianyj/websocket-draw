/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.service;


import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserInfoSo;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;

import java.util.List;

/**
 * 用户信息service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public interface GoldFishUserInfoService {

    /**
     * 查询用户列表
     * @param userInfoSo
     * @return
     */
    public List<GoldFishUserInfoRo> queryUserList(GoldFishUserInfoSo userInfoSo);

    /**
     * 查询单个用户
     * @param userInfoSo
     * @return
     */
    public GoldFishUserInfoRo querySingleUser(GoldFishUserInfoSo userInfoSo);

    /**
     * 查询用户数量
     * @param userInfoSo
     * @return
     */
    public Long queryUserCount(GoldFishUserInfoSo userInfoSo);

    /**
     * 创建用户
     * @param userInfo
     */
    public void createUser(GoldFishUserInfo userInfo);

    /**
     * 修改用户
     * @param userInfo
     */
    public void updateUser(GoldFishUserInfo userInfo);

    /**
     * 通过token获取用户信息
     * @param token
     * @return
     */
    public GoldFishUserInfoRo getUserInfo(String token);



}

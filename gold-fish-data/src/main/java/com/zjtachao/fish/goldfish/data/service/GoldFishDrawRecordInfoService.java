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


import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishDrawRecordInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishDrawRecordInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishDrawRecordInfoSo;

import java.util.List;

/**
 * 抽奖记录service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public interface GoldFishDrawRecordInfoService {

    /**
     * 查询抽奖记录列表
     * @param drawRecordInfoSo
     * @return
     */
    public List<GoldFishDrawRecordInfoRo> queryDrawRecordInfoList(GoldFishDrawRecordInfoSo drawRecordInfoSo);

    /**
     * 查询单个抽奖记录
     * @param drawRecordInfoSo
     * @return
     */
    public GoldFishDrawRecordInfoRo querySingleDrawRecordInfo(GoldFishDrawRecordInfoSo drawRecordInfoSo);

    /**
     * 查询抽奖记录数量
     * @param drawRecordInfoSo
     * @return
     */
    public Long queryDrawRecordInfoCount(GoldFishDrawRecordInfoSo drawRecordInfoSo);

    /**
     * 创建抽奖记录
     * @param drawRecordInfo
     */
    public void createDrawRecordInfo(GoldFishDrawRecordInfo drawRecordInfo);

    /**
     * 删除抽奖记录
     * @param drawRecordInfo
     */
    public void deleteDrawRecordInfo(GoldFishDrawRecordInfo drawRecordInfo);
}

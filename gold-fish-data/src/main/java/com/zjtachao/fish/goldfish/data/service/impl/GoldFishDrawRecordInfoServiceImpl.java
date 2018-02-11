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

import com.zjtachao.fish.goldfish.data.mapper.GoldFishDrawRecordInfoMapper;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishDrawRecordInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishDrawRecordInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishDrawRecordInfoSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishDrawRecordInfoService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 抽奖记录service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishDrawRecordInfoServiceImpl implements GoldFishDrawRecordInfoService {

    /**  抽奖记录mapper */
    @Resource
    private GoldFishDrawRecordInfoMapper goldFishDrawRecordInfoMapper;

    /**
     * 查询抽奖记录列表
     * @param drawRecordInfoSo
     * @return
     */
    public List<GoldFishDrawRecordInfoRo> queryDrawRecordInfoList(GoldFishDrawRecordInfoSo drawRecordInfoSo){
        return goldFishDrawRecordInfoMapper.queryDrawRecordInfoList(drawRecordInfoSo);
    }

    /**
     * 查询单个抽奖记录
     * @param drawRecordInfoSo
     * @return
     */
    public GoldFishDrawRecordInfoRo querySingleDrawRecordInfo(GoldFishDrawRecordInfoSo drawRecordInfoSo){
        return goldFishDrawRecordInfoMapper.querySingleDrawRecordInfo(drawRecordInfoSo);
    }

    /**
     * 查询抽奖记录数量
     * @param drawRecordInfoSo
     * @return
     */
    public Long queryDrawRecordInfoCount(GoldFishDrawRecordInfoSo drawRecordInfoSo){
        return goldFishDrawRecordInfoMapper.queryDrawRecordInfoCount(drawRecordInfoSo);
    }

    /**
     * 创建抽奖记录
     * @param drawRecordInfo
     */
    public void createDrawRecordInfo(GoldFishDrawRecordInfo drawRecordInfo){
        Date date = new Date();
        drawRecordInfo.setCreateTime(date);
        drawRecordInfo.setModifyTime(date);
        goldFishDrawRecordInfoMapper.createDrawRecordInfo(drawRecordInfo);
    }

    /**
     * 删除抽奖记录
     * @param drawRecordInfo
     */
    public void deleteDrawRecordInfo(GoldFishDrawRecordInfo drawRecordInfo){
        drawRecordInfo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        drawRecordInfo.setModifyTime(new Date());
        goldFishDrawRecordInfoMapper.deleteDrawRecordInfo(drawRecordInfo);
    }
}

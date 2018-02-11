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

import com.zjtachao.fish.goldfish.data.mapper.GoldFishRoomInfoMapper;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishRoomInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishRoomInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishRoomInfoSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishRoomInfoService;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 房间信息service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishRoomInfoServiceImpl implements GoldFishRoomInfoService {


    /** 房间信息mapper*/
    @Resource
    private GoldFishRoomInfoMapper goldFishRoomInfoMapper;

    /**
     * 查询房间列表
     * @param roomInfoSo
     * @return
     */
    public List<GoldFishRoomInfoRo> queryRoomInfoList(GoldFishRoomInfoSo roomInfoSo){
        return goldFishRoomInfoMapper.queryRoomInfoList(roomInfoSo);
    }

    /**
     * 查询房间
     * @param roomInfoSo
     * @return
     */
    public GoldFishRoomInfoRo querySingleRoomInfo(GoldFishRoomInfoSo roomInfoSo){
        return goldFishRoomInfoMapper.querySingleRoomInfo(roomInfoSo);
    }

    /**
     * 查询房间数量
     * @param roomInfoSo
     * @return
     */
    public Long queryRoomInfoCount(GoldFishRoomInfoSo roomInfoSo){
        return goldFishRoomInfoMapper.queryRoomInfoCount(roomInfoSo);
    }

    /**
     * 创建房间
     * @param roomInfo
     */
    public void createRoomInfo(GoldFishRoomInfo roomInfo){
        Date date = new Date();
        roomInfo.setCreateTime(date);
        roomInfo.setModifyTime(date);
        goldFishRoomInfoMapper.createRoomInfo(roomInfo);
    }

    /**
     * 修改房间
     * @param roomInfo
     */
    public void updateRoomInfo(GoldFishRoomInfo roomInfo){
        Date date = new Date();
        roomInfo.setModifyTime(date);
        goldFishRoomInfoMapper.updateRoomInfo(roomInfo);
    }

    /**
     * 删除房间
     * @param roomInfo
     */
    public void deleteRoomInfo(GoldFishRoomInfo roomInfo){
        roomInfo.setDeleteFlag(WaterBootCommonContext.DeleteFlagContext.DELETE_YES.getCode());
        roomInfo.setModifyTime(new Date());
        goldFishRoomInfoMapper.deleteRoomInfo(roomInfo);
    }
}

/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.mapper;


import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishRoomInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishRoomInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishRoomInfoSo;

import java.util.List;

/**
 * 房间信息mapper
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public interface GoldFishRoomInfoMapper {


    /**
     * 查询房间列表
     * @param roomInfoSo
     * @return
     */
    public List<GoldFishRoomInfoRo> queryRoomInfoList(GoldFishRoomInfoSo roomInfoSo);

    /**
     * 查询房间用户
     * @param roomInfoSo
     * @return
     */
    public GoldFishRoomInfoRo querySingleRoomInfo(GoldFishRoomInfoSo roomInfoSo);

    /**
     * 查询房间数量
     * @param roomInfoSo
     * @return
     */
    public Long queryRoomInfoCount(GoldFishRoomInfoSo roomInfoSo);

    /**
     * 创建房间
     * @param roomInfo
     */
    public void createRoomInfo(GoldFishRoomInfo roomInfo);

    /**
     * 修改房间
     * @param roomInfo
     */
    public void updateRoomInfo(GoldFishRoomInfo roomInfo);

    /**
     * 删除房间
     * @param roomInfo
     */
    public void deleteRoomInfo(GoldFishRoomInfo roomInfo);
}

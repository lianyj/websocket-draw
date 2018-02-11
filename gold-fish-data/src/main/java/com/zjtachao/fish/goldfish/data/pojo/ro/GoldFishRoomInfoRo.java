/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.pojo.ro;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseRo;

import java.util.Date;

/**
 * 房间ro
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public class GoldFishRoomInfoRo extends WaterBootBaseRo {


    private static final long serialVersionUID = -1189201922271816290L;
    /** 主键id */
    private Long id;

    /** 用户编码 */
    private String userCode;

    /** 房间编码 */
    private String roomCode;

    /** 房间状态 */
    private Integer roomState;

    private Date createTime;
    private Date modifyTime;
    private String deleteFlag;

    /** 用户唯一id */
    private String roomUniqueId;

    public String getRoomUniqueId() {
        return roomUniqueId;
    }

    public void setRoomUniqueId(String roomUniqueId) {
        this.roomUniqueId = roomUniqueId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getRoomState() {
        return roomState;
    }

    public void setRoomState(Integer roomState) {
        this.roomState = roomState;
    }
}

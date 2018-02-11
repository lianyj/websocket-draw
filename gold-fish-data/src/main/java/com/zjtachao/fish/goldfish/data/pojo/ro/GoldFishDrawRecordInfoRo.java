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
import java.util.List;

/**
 * 抽奖记录ro
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public class GoldFishDrawRecordInfoRo extends WaterBootBaseRo {

    private static final long serialVersionUID = 211761202771460935L;
    /** 主键id */
    private Long id;

    /** 记录编码 */
    private String recordCode;

    /** 房间编码 */
    private String roomCode;

    /** 记录状态 */
    private Integer recordState;

    /** 用户编码 */
    private String userCode;

    /** 中奖状态 */
    private Integer prizeState;

    /** 中奖数量 */
    private Integer drawNumber;

    /** 创建时间 */
    private Date createTime;

    /** 创建时间 */
    private String createTimeStr;

    /** 中奖用户信息 */
    private GoldFishUserInfoRo winUserInfoRo;

    /** 用户信息 */
    private List<GoldFishUserInfoRo> userInfoRos;

    private Date modifyTime;
    private String deleteFlag;

    public GoldFishUserInfoRo getWinUserInfoRo() {
        return winUserInfoRo;
    }

    public void setWinUserInfoRo(GoldFishUserInfoRo winUserInfoRo) {
        this.winUserInfoRo = winUserInfoRo;
    }

    public Integer getPrizeState() {
        return prizeState;
    }

    public void setPrizeState(Integer prizeState) {
        this.prizeState = prizeState;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<GoldFishUserInfoRo> getUserInfoRos() {
        return userInfoRos;
    }

    public void setUserInfoRos(List<GoldFishUserInfoRo> userInfoRos) {
        this.userInfoRos = userInfoRos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public Integer getRecordState() {
        return recordState;
    }

    public void setRecordState(Integer recordState) {
        this.recordState = recordState;
    }

    public Integer getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(Integer drawNumber) {
        this.drawNumber = drawNumber;
    }
}

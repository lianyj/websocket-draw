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

import java.util.List;

/**
 * 显示房间用户人数和用户信息
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public class GoldFishHomeUserRo extends WaterBootBaseRo{

    private static final long serialVersionUID = 3330263536433110723L;
    /** 主键id */
    private Long roomId;

    /** 房间创建者Token */
    private String roomUserToken;

    /** 房间编码 */
    private String roomCode;

    /** 房间二维码 */
    private String wxacode;

    /** 房间状态 */
    private Integer roomState;

    /** 用户数量 */
    private Long userNumber;

    /** 用户信息 */
    private List<GoldFishUserInfoRo> userInfoRos;

    public String getWxacode() {
        return wxacode;
    }

    public void setWxacode(String wxacode) {
        this.wxacode = wxacode;
    }

    public String getRoomUserToken() {
        return roomUserToken;
    }

    public void setRoomUserToken(String roomUserToken) {
        this.roomUserToken = roomUserToken;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
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

    public Long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Long userNumber) {
        this.userNumber = userNumber;
    }

    public List<GoldFishUserInfoRo> getUserInfoRos() {
        return userInfoRos;
    }

    public void setUserInfoRos(List<GoldFishUserInfoRo> userInfoRos) {
        this.userInfoRos = userInfoRos;
    }
}

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

/**
 * 发送消息service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public interface GoldFishSendMessageService {

    /**
     * 房间指定人发送消息
     * @param message
     * @return
     */
    public void sendUserMessage(String message, String roomCode, String token,String cmd,String exitToken);



    /**
     * 群发房间所有人发送消息
     * @param message
     * @return
     */
    public void sendMessage(String message, String roomCode,String cmd,String exitToken);
}

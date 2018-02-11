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
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishMessageInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishSessionAddressRo;
import com.zjtachao.fish.goldfish.data.service.GoldFishSendMessageService;
import com.zjtachao.fish.goldfish.data.socket.GoldFishWebSocketMap;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.water.common.data.WaterRedis;
import com.zjtachao.fish.water.common.tool.WaterHttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 发送消息service
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */

@Service
public class GoldFishSendMessageServiceImpl implements GoldFishSendMessageService{

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(GoldFishSendMessageServiceImpl.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    @Autowired
    public Environment waterEnv;



    /**
     * 群发房间所有人发送消息
     * @param message
     * @return
     */
    @Async
    public void sendMessage(String message,String roomCode,String cmd,String exitToken){
        try{
            Set<String> tokenSet = waterRedis.smembers(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
            if(null != tokenSet && !tokenSet.isEmpty()){
                for (String token :tokenSet ){
                    String sessionAddress =  waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
                    if(null != sessionAddress && !"".equals(sessionAddress)){
                        GoldFishSessionAddressRo sessionAddressRo = JSON.parseObject(sessionAddress,GoldFishSessionAddressRo.class);
                        if(null != sessionAddressRo && null != sessionAddressRo.getAddress()){
                            sendHttpMessage(sessionAddressRo.getAddress(),roomCode,token,message,sessionAddressRo.getSessionId(),cmd,exitToken);
                        }else {
                            logger.info("房间地址为空");
                        }
                    }
                }

                if("CLOSE".equals(cmd)){
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
                }

            }else {
                logger.info("房间下的用户为空");
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
        }
    }

    /**
     * http发送数据
     */
    private void sendHttpMessage(String address,String roomCode,String token,String message,String sessionId,String cmd,String exitToken){
        try{
            String restUrl = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_SEND_MESSAGE_REST_URL);
            String prefix  = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_HTTP_PREFIX);
            String url = prefix + address + restUrl;
            GoldFishMessageInfo goldFishMessageInfo = new GoldFishMessageInfo();
            goldFishMessageInfo.setClientId(sessionId);
            goldFishMessageInfo.setMessage(message);
            goldFishMessageInfo.setToken(token);
            goldFishMessageInfo.setRoomCode(roomCode);
            String result = WaterHttpUtil.httpPost(url,JSON.toJSONString(goldFishMessageInfo));
            logger.info("发送消息result:  "+result);
           //删除房间用户信息
            if(null != cmd){
                if("CLOSE".equals(cmd)){
                    GoldFishWebSocketMap.removeSession(sessionId);
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
                    waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
                    if(null != exitToken && token.equals(exitToken)){
                        waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
                        waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
                    }
                }
                if("EXIT".equals(cmd) && null != exitToken && token.equals(exitToken)){
                    GoldFishWebSocketMap.removeSession(sessionId);
                    //退出房间
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
//                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
                    waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
                }
                if("LEAVE".equals(cmd) && null != exitToken && token.equals(exitToken)){
                    GoldFishWebSocketMap.removeSession(sessionId);
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
                    waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * 房间指定人发送消息
     * @param message
     * @return
     */
    @Async
    public void sendUserMessage(String message,String roomCode,String token,String cmd,String exitToken) {
        try{
            String sessionAddress =  waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
            if(null != sessionAddress && !"".equals(sessionAddress)) {
                GoldFishSessionAddressRo sessionAddressRo = JSON.parseObject(sessionAddress, GoldFishSessionAddressRo.class);
                if (null != sessionAddressRo && null != sessionAddressRo.getAddress()) {
                    sendHttpMessage(sessionAddressRo.getAddress(), roomCode, token, message,sessionAddressRo.getSessionId(),cmd,exitToken);
                } else {
                    logger.info("用户的地址为空");
                }
            }
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
        }
    }
}

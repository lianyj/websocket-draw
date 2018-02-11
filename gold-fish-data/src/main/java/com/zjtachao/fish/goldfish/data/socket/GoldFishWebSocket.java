/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.socket;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishWebSocketInfoRo;
import com.zjtachao.fish.goldfish.data.service.GoldFishWebSocketService;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.goldfish.data.util.tools.GoldFishSpringUtil;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.data.WaterRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * websocket推送
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Controller
@ServerEndpoint(value = "/scan/code")
public class GoldFishWebSocket {

    /** 日志 **/
    private static final Logger logger = LoggerFactory.getLogger(WaterBootBaseController.class);

    /** handle **/
    private GoldFishWebSocketService goldFishWebSocketService;

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private Environment waterEnv;

    private WaterRedis waterRedis;

    /**
     * 连接建立成功调用的方法  (用户进入房间)
     */
    @OnOpen
    public void onOpen(Session session) {
        try{
            this.session = session;
            GoldFishWebSocketMap.addSession(session.getId(),this);
            logger.info("websocket连接开启，id:"+ session.getId());
//            if(null == waterRedis){
//                waterRedis = GoldFishSpringUtil.getBean(WaterRedis.class);
//            }
//            if(null == waterEnv){
//                waterEnv = GoldFishSpringUtil.getBean(Environment.class);
//            }
//            String address = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_LOCAL_SERVER_URL);
//            //存缓存
//            waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+session.getId(),address,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
        }catch (Exception ex){
            logger.error("连接出错" , ex);
        }
    }



    /**
     * 连接关闭调用的方法(用户退出房间)
     */
    @OnClose
    public void onClose() {
        try {
            GoldFishWebSocketMap.removeSession(session.getId());
            logger.info("websocket连接关闭，id:"+session.getId());
            String token = GoldFishWebSocketMap.getId(session.getId());
            if(null != token && !"".equals(token)){
                if(null == goldFishWebSocketService){
                    goldFishWebSocketService = GoldFishSpringUtil.getBean(GoldFishWebSocketService.class);
                }
                if(null == waterRedis){
                    waterRedis = GoldFishSpringUtil.getBean(WaterRedis.class);
                }
                String normalRoomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
                if(null !=normalRoomCode && !"".equals(normalRoomCode)){
                    GoldFishWebSocketInfoRo fishWebSocketInfoRo = new GoldFishWebSocketInfoRo();
                    fishWebSocketInfoRo.setCmd("LEAVE");
                    fishWebSocketInfoRo.setToken(token);
                    fishWebSocketInfoRo.setRoomCode(normalRoomCode);
                    goldFishWebSocketService.handle(JSON.toJSONString(fishWebSocketInfoRo), session);
                }
            }
            GoldFishWebSocketMap.removeId(session.getId());
        }catch (Exception ex){
            logger.error("关闭连接出错" , ex);
        }
    }


    /**
     * 接收客户端消息
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        logger.info("来自客户端的消息:" + message);
        try {
            if(null == goldFishWebSocketService){
                goldFishWebSocketService = GoldFishSpringUtil.getBean(GoldFishWebSocketService.class);
            }
            goldFishWebSocketService.handle(message , session);
            logger.info(message);
        }catch (Exception ex){
            logger.error("接收客户端消息" , ex);
        }
    }


    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
//        logger.warn(session.getId() + ": 发生错误:" , error);
    }


    /** 推送信息 */
    public  synchronized void sendMessage(String message)  {
        try {
            if(null != this.session && this.session.isOpen()){
                this.session.getBasicRemote().sendText(message);
//                this.session.getAsyncRemote().sendText(message);
            }else {
                logger.info("session已关闭，推送信息失败");
            }
        } catch (Exception ex) {
            logger.error("发送消息错误:"+message , ex);
        }
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

}

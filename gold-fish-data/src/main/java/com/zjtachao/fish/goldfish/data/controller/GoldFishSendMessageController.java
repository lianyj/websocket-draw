/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.controller;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishMessageInfo;
import com.zjtachao.fish.goldfish.data.socket.GoldFishWebSocket;
import com.zjtachao.fish.goldfish.data.socket.GoldFishWebSocketMap;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * 发送webSocket消息
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Controller
@Path("/webSocket")
public class GoldFishSendMessageController extends WaterBootBaseController {


    /**
     * 接收webSocket消息
     */
    @POST
    @Path("/message/send")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<String> sendMessage(
            String jsonContent,
            @Context HttpServletResponse response){
        WaterBootResultBean<String> rest = new WaterBootResultBean<String>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            if(null != jsonContent && !"".equals(jsonContent) && !jsonContent.isEmpty() ){
                GoldFishMessageInfo goldFishMessageInfo = JSON.parseObject(jsonContent,GoldFishMessageInfo.class);
                boolean flag = true;
                if(null == goldFishMessageInfo.getClientId() && !"".equals(goldFishMessageInfo.getClientId())){
                    rest.setMsg("sessionid为空");
                    flag = false;
                }
                if(flag && (null == goldFishMessageInfo.getMessage() && !"".equals(goldFishMessageInfo.getMessage()))){
                    rest.setMsg("消息为空");
                    flag = false;
                }
                if(flag && (null == goldFishMessageInfo.getRoomCode() && !"".equals(goldFishMessageInfo.getRoomCode()))){
                    rest.setMsg("房间编码为空");
                    flag = false;
                }
                if(flag && (null == goldFishMessageInfo.getToken() && !"".equals(goldFishMessageInfo.getToken()))){
                    rest.setMsg("用户编码为空");
                    flag = false;
                }

                if(flag){
                    String address = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_LOCAL_SERVER_URL);
                    logger.info("接收到的服务器address："+ address +"  ,发送的信息是："+  goldFishMessageInfo.getMessage());
                    GoldFishWebSocket goldFishWebSocket =  GoldFishWebSocketMap.getSession(goldFishMessageInfo.getClientId());
                    if(null != goldFishWebSocket){
                        goldFishWebSocket.sendMessage(goldFishMessageInfo.getMessage());
                        rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        rest.setMsg("发送消息成功");
                    }else {
                        rest.setMsg("");
                    }
                }
            }else {
                rest.setMsg("发送消息失败，传递信息出错！");
            }
        }catch(Exception ex){
            logger.error("发送消息失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("发送消息失败，请稍后再试，原因：服务器出错！");
        }
        logger.info(JSON.toJSONString(rest));
        return rest;
    }
}

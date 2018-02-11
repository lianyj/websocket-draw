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
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishDrawRecordInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserPrizeInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserRoomRel;
import com.zjtachao.fish.goldfish.data.pojo.ro.*;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishDrawRecordInfoSo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserPrizeInfoSo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserRoomRelSo;
import com.zjtachao.fish.goldfish.data.service.*;
import com.zjtachao.fish.goldfish.data.socket.GoldFishWebSocketMap;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.goldfish.data.util.context.GoldFishCommonContext;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.data.WaterRedis;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.util.*;

/**
 * TODO
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Service
public class GoldFishWebSocketServiceImpl implements GoldFishWebSocketService{

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(GoldFishWebSocketServiceImpl.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    @Autowired
    public Environment waterEnv;

    /** 用户service */
    @Autowired
    private GoldFishUserInfoService goldFishUserInfoService;

    /** 用户房间关联service */
    @Autowired
    private GoldFishUserRoomRelService goldFishUserRoomRelService;

    /** 房间信息service */
    @Autowired
    private GoldFishRoomInfoService goldFishRoomInfoService;

    /** 发送信息service */
    @Autowired
    private GoldFishSendMessageService goldFishSendMessageService;

    /** 抽奖记录service */
    @Autowired
    private GoldFishDrawRecordInfoService goldFishDrawRecordInfoService;

    /** 用户中奖名单service */
    @Autowired
    private GoldFishUserPrizeInfoService goldFishUserPrizeInfoService;




    /**
     * 总控
     * @param json
     * @param session
     */
    @Override
    public void handle(String json, Session session){
        WaterBootResultBean<GoldFishHomeUserRo> rest = new WaterBootResultBean<GoldFishHomeUserRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            GoldFishWebSocketInfoRo fishWebSocketInfoRo = null;
            boolean flag = true;
            if(null == json || "".equals(json)){
                rest.setMsg( "参数错误");
                flag = false;
            }
            if(flag && !((json.contains("{") && json.contains("}")) || (json.contains("[") && json.contains("]")))){
                rest.setMsg("数据必须是json格式");
                flag = false;
            }
            if(flag) {
                fishWebSocketInfoRo = JSON.parseObject(json, GoldFishWebSocketInfoRo.class);
                if (null == fishWebSocketInfoRo || null == fishWebSocketInfoRo.getToken()) {
                    rest.setMsg("未找到token参数");
                    flag = false;
                }
                if (flag && (null == fishWebSocketInfoRo.getCmd() || "".equals(fishWebSocketInfoRo.getCmd()))) {
                    rest.setMsg("未找到命令参数");
                    flag = false;
                }
                if (flag && (null == fishWebSocketInfoRo.getRoomCode() || "".equals(fishWebSocketInfoRo.getRoomCode()))) {
                    rest.setMsg("未找到房间编码");
                    flag = false;
                }
            }

            if(flag) {
                String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY + fishWebSocketInfoRo.getRoomCode());
                if (null != createToken && !"".equals(createToken)) {
                    String roomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY + createToken);
                    if (null == roomCode || "".equals(roomCode)) {
                        rest.setCode(7);
                        goldFishSendMessageService.sendMessage(JSON.toJSONString(rest), roomCode,null,null);
                        flag = false;
                    }
                }
            }

            if(flag){
                if(fishWebSocketInfoRo.getCmd().equals("JOIN")){
                    //进入房间
                    GoldFishWebSocketMap.addId(session.getId(),fishWebSocketInfoRo.getToken());
                    userJoinRoom(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),rest,fishWebSocketInfoRo.getCmd(),session.getId() );
                } else if (fishWebSocketInfoRo.getCmd().equals("EXIT")) {
                    // 退出房间
                    userExitRoom(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,rest,fishWebSocketInfoRo.getCmd() );
                } else if (fishWebSocketInfoRo.getCmd().equals("CLOSE")) {
                    // 关闭房间
                    userCloseRoom(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,rest,fishWebSocketInfoRo.getCmd() );
                } else if (fishWebSocketInfoRo.getCmd().equals("LEAVE")) {
                    // 关闭房间
                    userLeaveRoom(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,rest,fishWebSocketInfoRo.getCmd() );
                }else if (fishWebSocketInfoRo.getCmd().equals("START")) {
                    // 开始抽奖 5  10
                    userStartDraw(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,fishWebSocketInfoRo.getDrawNumber(),fishWebSocketInfoRo.getCmd() );
                }else if (fishWebSocketInfoRo.getCmd().equals("RECORD")) {
                    // 抽奖记录 6
                    userGetRecord(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,fishWebSocketInfoRo.getRecordCode(),fishWebSocketInfoRo.getCmd() );
                }else if (fishWebSocketInfoRo.getCmd().equals("DELETE")) {
                    // 删除抽奖记录 6
                    userDeleteRecord(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,fishWebSocketInfoRo.getRecordCode(),fishWebSocketInfoRo.getCmd() );
                }else if (fishWebSocketInfoRo.getCmd().equals("HEARTBEAT")) {
                    //心跳
                    logger.info("心跳 连接开启，id:"+ session.getId());
//                    GoldFishWebSocketMap.addUser(fishWebSocketInfoRo.getToken(),session.getId());
                    heartBeat(fishWebSocketInfoRo.getRoomCode(),fishWebSocketInfoRo.getToken(),session,rest,fishWebSocketInfoRo.getCmd() );
                }
            }else {
                logger.info(JSON.toJSONString(rest));
            }
        }catch (Exception ex){
            logger.error("参数错误" , ex);
        }
    }

    /**
     * 用户进入房间的方法
     *
     */
    public void userJoinRoom(String roomCode,String token,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd,String sessionId){
        String address = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_LOCAL_SERVER_URL);
        //存缓存
        GoldFishSessionAddressRo sessionAddressRo = new GoldFishSessionAddressRo();
        sessionAddressRo.setAddress(address);
        sessionAddressRo.setSessionId(sessionId);
        waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token,JSON.toJSONString(sessionAddressRo),GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);

       //1.创建房间，并将用户放入房间
        waterRedis.sadd(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
        waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,60);
        waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token,roomCode,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
        //存房间用户关联信息
        GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(token);
        if(null != goldFishUserInfoRo){
            GoldFishUserRoomRelSo userRoomRelSo = new GoldFishUserRoomRelSo();
            userRoomRelSo.setUserCode(goldFishUserInfoRo.getUserCode());
            List<GoldFishUserRoomRelRo> goldFishUserRoomRelRos = goldFishUserRoomRelService.queryUserRoomRelList(userRoomRelSo);
            if(null != goldFishUserRoomRelRos && !goldFishUserRoomRelRos.isEmpty()){
                logger.info("用户已和房间关联！");
            }else {
                GoldFishUserRoomRel userRoomRel = new GoldFishUserRoomRel();
                userRoomRel.setRoomCode(roomCode);
                userRoomRel.setUserCode(goldFishUserInfoRo.getUserCode());
                goldFishUserRoomRelService.createUserRoomRel(userRoomRel);
            }

        }
        //2.通知房间内所有用户
        //发送消息
        rest.setCode(0);
        setAndSendUserList(roomCode,rest,cmd,null);
    }

    /**
     * 发送抽奖记录的方法
     *
     */
    public void  userGetRecord(String roomCode, String token,Session session,String recordCode,String cmd) {
        WaterBootResultBean<GoldFishDrawRecordInfoRo> rest = new WaterBootResultBean<GoldFishDrawRecordInfoRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            //查询房间是是否存在
            String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
            if(null != createToken){
                List<GoldFishDrawRecordInfoRo> drawRecordInfoRos = null;
                //查询中奖记录缓存
                String recordInfo = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode);
                if(null != recordInfo && !"".equals(recordInfo)){
                    drawRecordInfoRos = JSON.parseArray(recordInfo,GoldFishDrawRecordInfoRo.class);
                    Long recordTime = waterRedis.ttl(waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode));
                    if(recordTime.longValue()<2400){
                        waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                    }
                }else {
                    //查询该房间抽奖记录
                    GoldFishDrawRecordInfoSo drawRecordInfoSo = new GoldFishDrawRecordInfoSo();
                    drawRecordInfoSo.setRoomCode(roomCode);
                    drawRecordInfoRos = goldFishDrawRecordInfoService.queryDrawRecordInfoList(drawRecordInfoSo);
                    if (null != drawRecordInfoRos && !drawRecordInfoRos.isEmpty()) {
                        for (int i = 0; i < drawRecordInfoRos.size(); i++) {
                            //查询该房间抽奖记录下所有用户
                            GoldFishUserPrizeInfoSo goldFishUserPrizeInfoSo = new GoldFishUserPrizeInfoSo();
                            goldFishUserPrizeInfoSo.setRecordCode(drawRecordInfoRos.get(i).getRecordCode());
                            List<GoldFishUserInfoRo> userInfoRos = goldFishUserPrizeInfoService.queryPrizeUserList(goldFishUserPrizeInfoSo);
                            if (null != userInfoRos && !userInfoRos.isEmpty()) {
                                drawRecordInfoRos.get(i).setUserInfoRos(userInfoRos);
                            }
                            String createTimeStr = WaterDateUtil.date2Str(drawRecordInfoRos.get(i).getCreateTime(), "MM-dd HH:mm");
                            drawRecordInfoRos.get(i).setCreateTimeStr(createTimeStr);
                        }
                    }
                    //设置缓存
                    waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode,JSON.toJSONString(drawRecordInfoRos),GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                }
                if(null != drawRecordInfoRos && !drawRecordInfoRos.isEmpty()){
                    rest.setCode(6);
                    rest.setRst(drawRecordInfoRos);
                    rest.setMsg("查询房间抽奖名单成功！");
                }else {
                    rest.setRst(drawRecordInfoRos);
                    rest.setCode(6);
                    rest.setMsg("该房间没有中奖记录！");
                }
                //发送抽奖记录消息
                goldFishSendMessageService.sendUserMessage(JSON.toJSONString(rest), roomCode,token,cmd,null);
            }else {
                logger.info("房间不存在！");
            }
        }catch(Exception ex){
            logger.error("查询房间中奖记录失败！" , ex);
        }
    }

    /**
     * 创建者 删除抽奖记录的方法
     *
     */
    public void  userDeleteRecord(String roomCode, String token,Session session,String recordCode,String cmd) {
        WaterBootResultBean<GoldFishDrawRecordInfoRo> rest = new WaterBootResultBean<GoldFishDrawRecordInfoRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            //查询房间是是否存在
            String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
            if(null != createToken && createToken.equals(token)){
                //删除获奖记录
                GoldFishDrawRecordInfo drawRecordInfo = new GoldFishDrawRecordInfo();
                drawRecordInfo.setRecordCode(recordCode);
                goldFishDrawRecordInfoService.deleteDrawRecordInfo(drawRecordInfo);
                //删除获奖名单
                GoldFishUserPrizeInfo goldFishUserPrizeInfo = new GoldFishUserPrizeInfo();
                goldFishUserPrizeInfo.setRecordCode(recordCode);
                goldFishUserPrizeInfoService.deleteUserPrizeInfo(goldFishUserPrizeInfo);
                rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                rest.setMsg("删除抽奖记录成功！");

                //删除抽奖记录
                List<GoldFishDrawRecordInfoRo> newRecordInfoRoList = new ArrayList<GoldFishDrawRecordInfoRo>();
                String recordInfo = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode);
                if(null != recordInfo && !"".equals(recordInfo)){
                    List<GoldFishDrawRecordInfoRo> goldFishDrawRecordInfoRos = JSON.parseArray(recordInfo,GoldFishDrawRecordInfoRo.class);
                    if(null != goldFishDrawRecordInfoRos && !goldFishDrawRecordInfoRos.isEmpty()){
                        for (int i =0; i<goldFishDrawRecordInfoRos.size();i++){
                            if(!goldFishDrawRecordInfoRos.get(i).getRecordCode().equals(recordCode)){
                                newRecordInfoRoList.add(goldFishDrawRecordInfoRos.get(i));
                            }
                        }
                    }
                }
                //删除抽奖记录
                waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode,JSON.toJSONString(newRecordInfoRoList),GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                //发送删除抽奖记录消息
                rest.setCode(6);
                rest.setRst(newRecordInfoRoList);
                goldFishSendMessageService.sendMessage(JSON.toJSONString(rest),roomCode,cmd,null);
            }else {
                logger.info("您不是抽奖发起者，无法删除房间抽奖记录");
            }
        }catch(Exception ex){
            logger.error("删除房间抽奖记录失败！" , ex);
        }

    }


    /**
     * 创建者发送抽奖的方法
     *
     */
    public void  userStartDraw(String roomCode, String token,Session session,int drawNumber,String cmd) {
            WaterBootResultBean<GoldFishDrawRecordInfoRo> rest = new WaterBootResultBean<GoldFishDrawRecordInfoRo>();
            try{
                rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
                //查询房间是是否存在
                String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
                if(null != createToken && createToken.equals(token)){
                    //查询房间下所有用户
                    //调用websocket 发送抽奖通知  --给房间所有人
                    Set<String> tokenSet = waterRedis.smembers(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
                    if(null != tokenSet && !tokenSet.isEmpty()) {
                        //保存用户抽奖记录
                        GoldFishDrawRecordInfo goldFishDrawRecordInfo = new GoldFishDrawRecordInfo();
                        String recordCode = WaterUUIDUtil.getUUID();
                        goldFishDrawRecordInfo.setDrawNumber(drawNumber);
                        GoldFishUserInfoRo createUserInfoRo = goldFishUserInfoService.getUserInfo(token);
                        goldFishDrawRecordInfo.setUserCode(createUserInfoRo.getUserCode());
                        goldFishDrawRecordInfo.setRoomCode(roomCode);
                        goldFishDrawRecordInfo.setRecordCode(recordCode);
                        goldFishDrawRecordInfo.setRecordState(GoldFishCommonContext.DrawRecordStateContext.RECORD_NORMAL.getCode());
                        goldFishDrawRecordInfoService.createDrawRecordInfo(goldFishDrawRecordInfo);
                        //随机取出用户数
                        Set<String> winUserTokenSet = new HashSet<String>();
                        while(winUserTokenSet.size() < drawNumber){
                            String userToken =  waterRedis.srandom(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
                            winUserTokenSet.add(userToken);
                        }
                        List<GoldFishUserInfoRo> userWinInfoRoList = new ArrayList<GoldFishUserInfoRo>();
                        for(String winUserToken :winUserTokenSet){
                            GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(winUserToken);
                            //保存用户抽奖名单
                            GoldFishUserPrizeInfo goldFishUserPrizeInfo = new GoldFishUserPrizeInfo();
                            goldFishUserPrizeInfo.setUserCode(goldFishUserInfoRo.getUserCode());
                            goldFishUserPrizeInfo.setRecordCode(recordCode);
                            goldFishUserPrizeInfoService.createUserPrizeInfo(goldFishUserPrizeInfo);
                            //设置用户抽奖名单
                            userWinInfoRoList.add(goldFishUserInfoRo);
                        }
                        logger.info("调用websocket 发送抽奖通知");
                        //调用websocket 发送抽奖通知  --给房间所有人
                        for (String userToken : tokenSet) {
                            //通过token获取用户信息
                            GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(userToken);
                            if(null != goldFishUserInfoRo){
                                //设置抽奖信息
                                GoldFishDrawRecordInfoRo goldFishDrawRecordInfoRo = new GoldFishDrawRecordInfoRo();
                                goldFishDrawRecordInfoRo.setDrawNumber(goldFishDrawRecordInfo.getDrawNumber());
                                goldFishDrawRecordInfoRo.setRoomCode(roomCode);
                                goldFishDrawRecordInfoRo.setRecordCode(recordCode);
                                //中奖者返回中奖者头像，如果不中奖返回中奖者头像
                                int prizeState = GoldFishCommonContext.UserPrizeStateContext.NOT_WINNING.getCode();
                                for (int i = 0; i < userWinInfoRoList.size(); i++) {
                                    if (goldFishUserInfoRo.getUserCode().equals(userWinInfoRoList.get(i).getUserCode())) {
                                        prizeState = GoldFishCommonContext.UserPrizeStateContext.HAVE_WINNING.getCode();
                                        goldFishDrawRecordInfoRo.setWinUserInfoRo(userWinInfoRoList.get(i));
                                        break;
                                    }
                                }
                                goldFishDrawRecordInfoRo.setPrizeState(prizeState);
                                //如果是房间创建者，发送所有用户中奖信息，否则返回一个用户头像
                                if(createToken.equals(userToken)){
                                    rest.setCode(10);
                                    goldFishDrawRecordInfoRo.setUserInfoRos(userWinInfoRoList);
                                }else {
                                    rest.setCode(5);
                                    if(null == goldFishDrawRecordInfoRo.getWinUserInfoRo()){
                                        goldFishDrawRecordInfoRo.setWinUserInfoRo(userWinInfoRoList.get(0));
                                    }
                                }
                                rest.setMsg("开始进行抽奖了！");
                                rest.setRst(goldFishDrawRecordInfoRo);
                                goldFishSendMessageService.sendUserMessage(JSON.toJSONString(rest), roomCode, userToken,cmd,null);
                            }
                        }

                        //设置中奖记录缓存
                        List<GoldFishDrawRecordInfoRo> newRecordInfoRoList = new ArrayList<GoldFishDrawRecordInfoRo>();
                        GoldFishDrawRecordInfoRo goldFishDrawRecordInfoRo = new GoldFishDrawRecordInfoRo();
                        goldFishDrawRecordInfoRo.setDrawNumber(goldFishDrawRecordInfo.getDrawNumber());
                        goldFishDrawRecordInfoRo.setRoomCode(goldFishDrawRecordInfo.getRoomCode());
                        goldFishDrawRecordInfoRo.setRecordCode(recordCode);
                        goldFishDrawRecordInfoRo.setUserInfoRos(userWinInfoRoList);
                        goldFishDrawRecordInfoRo.setCreateTime(new Date());
                        goldFishDrawRecordInfoRo.setCreateTimeStr(WaterDateUtil.date2Str(new Date(),"yyyy-MM-dd HH:mm:ss"));
                        newRecordInfoRoList.add(goldFishDrawRecordInfoRo);
                        String recordInfo = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+goldFishDrawRecordInfo.getRoomCode());
                        if(null != recordInfo && !"".equals(recordInfo)){
                            List<GoldFishDrawRecordInfoRo> goldFishDrawRecordInfoRos = JSON.parseArray(recordInfo,GoldFishDrawRecordInfoRo.class);
                            if(null != goldFishDrawRecordInfoRos && !goldFishDrawRecordInfoRos.isEmpty()){
                                newRecordInfoRoList.addAll(goldFishDrawRecordInfoRos);
                            }
                        }
                        waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+goldFishDrawRecordInfo.getRoomCode(),JSON.toJSONString(newRecordInfoRoList),GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                    }else {
                        logger.info("随机抽奖失败，此房间里没有用户！");
                    }
                }else {
                    logger.info("不是创建者，不能发起抽奖");
                }
            }catch(Exception ex){
                logger.error("发起抽奖失败！" , ex);
            }
    }


    /**
     * 创建者关闭房间
     * @param roomCode
     * @param token
     * @param session
     * @param rest
     */
    public void  userCloseRoom(String roomCode, String token,Session session,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd) {
        //退出房间
        waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
        //创建者关闭房间
        String createRoomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
        if(null != createRoomCode && createRoomCode.equals(roomCode)){
            rest.setCode(7);
            //发送消息
            goldFishSendMessageService.sendMessage(JSON.toJSONString(rest),roomCode,cmd,token);
        }
    }

    /**
     * 用户离开房间的方法
     *
     */
    public void  userLeaveRoom(String roomCode, String token,Session session,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd) {
        String sessionAddress =  waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
        if(null != sessionAddress && !"".equals(sessionAddress)) {
            GoldFishSessionAddressRo sessionAddressRo = JSON.parseObject(sessionAddress, GoldFishSessionAddressRo.class);
            GoldFishWebSocketMap.removeSession(sessionAddressRo.getSessionId());
        }
        waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS + token);
        waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY + roomCode, token);
        rest.setCode(0);
        //发送消息
        setAndSendUserList(roomCode, rest,cmd,token);
    }

    /**
     * 用户退出房间的方法
     *
     */
    public void  userExitRoom(String roomCode, String token,Session session,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd) {
        String sessionAddress =  waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
        if(null != sessionAddress && !"".equals(sessionAddress)) {
            GoldFishSessionAddressRo sessionAddressRo = JSON.parseObject(sessionAddress, GoldFishSessionAddressRo.class);
            GoldFishWebSocketMap.removeSession(sessionAddressRo.getSessionId());
        }
        //退出房间
        waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
        waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
        waterRedis.sdel(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,token);
        rest.setCode(0);
        //发送消息
        setAndSendUserList(roomCode, rest,cmd,token);
    }

    /**
     * 心跳
     * @return
     */
    private String heartBeat(String roomCode,String token,Session session,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd){
        WaterBootResultBean<String> result = new WaterBootResultBean<String>();
        try {
            Long exireTime = waterRedis.ttl(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
            if(null != exireTime && exireTime.longValue() <2400) {
                waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode,60);
            }
            Long tokeneTime = waterRedis.ttl(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
            if(null != tokeneTime && tokeneTime.longValue() <2400) {
                waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token,60);
            }
            Long addressTime = waterRedis.ttl(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token);
            if(null != addressTime && addressTime.longValue() <2400) {
                waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_ADDRESS+token,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
            }
            Long nomralTime = waterRedis.ttl(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
            if(null != nomralTime && nomralTime.longValue() <2400) {
                waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
            }
            result.setRst("Heart Beat!");
            result.setCode(8);
            result.setMsg("HEART_BEAT OK!");

            goldFishSendMessageService.sendUserMessage(JSON.toJSONString(result),roomCode,token,cmd,null);
        }catch (Exception ex){
            logger.error(ex.getMessage() , ex);
            result.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            result.setMsg("服务器出错");
        }
        return JSON.toJSONString(result);
    }


    /**
     * 发送用户信息
     * @param roomCode
     */
    private void setAndSendUserList(String roomCode,WaterBootResultBean<GoldFishHomeUserRo> rest,String cmd,String exitToken){
        GoldFishHomeUserRo goldFishHomeUserRo = new GoldFishHomeUserRo();
        String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY + roomCode);
        goldFishHomeUserRo.setRoomUserToken(createToken);
        Set<String> tokenSet = waterRedis.smembers(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
        if(null != tokenSet && !tokenSet.isEmpty()) {
            List<GoldFishUserInfoRo> goldFishUserInfoList = new ArrayList<GoldFishUserInfoRo>();
            for(String userToken : tokenSet) {
                if(null != userToken && !"".equals(userToken)) {
                    GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(userToken);
                    if(null != goldFishUserInfoRo) {
                        goldFishUserInfoList.add(goldFishUserInfoRo);
                    }

                }
            }
            if(!goldFishUserInfoList.isEmpty()) {
                goldFishHomeUserRo.setUserInfoRos(goldFishUserInfoList);
                rest.setRst(goldFishHomeUserRo);
                goldFishSendMessageService.sendMessage(JSON.toJSONString(rest),roomCode,cmd,exitToken);
            }else {
                logger.info("发送的信息为空！");
            }
        }
    }
}

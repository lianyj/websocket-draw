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
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishDrawRecordInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishRoomInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserPrizeInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserRoomRel;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishCoverContentInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishDrawRecordInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserPrizeInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishDrawRecordInfoSo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserPrizeInfoSo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserRoomRelSo;
import com.zjtachao.fish.goldfish.data.service.*;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.goldfish.data.util.context.GoldFishCommonContext;
import com.zjtachao.fish.goldfish.data.util.tools.GoldFishUniqueIdUtil;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.*;


/**
 * 抽奖接口
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Controller
@Path("/draw")
public class GoldFishDrawController extends WaterBootBaseController {


    /** 房间service */
    @Autowired
    private GoldFishRoomInfoService goldFishRoomInfoService;

    /** 用户service */
    @Autowired
    private GoldFishUserInfoService goldFishUserInfoService;

    /** 用户房间关联service */
    @Autowired
    private GoldFishUserRoomRelService goldFishUserRoomRelService;

    /** 抽奖记录service */
    @Autowired
    private GoldFishDrawRecordInfoService goldFishDrawRecordInfoService;

    /** 用户中奖名单service */
    @Autowired
    private GoldFishUserPrizeInfoService goldFishUserPrizeInfoService;

    /** 发送消息service */
    @Autowired
    private GoldFishSendMessageService goldFishSendMessageService;

    /**
     * 查询首页内容
     */
    @GET
    @Path("/get/cover/info")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<GoldFishCoverContentInfoRo> getCoverInfo(
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token){
        WaterBootResultBean<GoldFishCoverContentInfoRo> rest = new WaterBootResultBean<GoldFishCoverContentInfoRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
//            List<GoldFishCoverContentInfoRo> coverContentInfoRos = new ArrayList<GoldFishCoverContentInfoRo>();
//            GoldFishCoverContentInfoRo coverContentInfoRo = new GoldFishCoverContentInfoRo();
//            coverContentInfoRo.setPicture("https://goldfish.zjtachao.com/banner.png");
//            coverContentInfoRo.setText("年会指某些社会团体一年举行一次的集会，是企业和组织一年一度的“家庭盛会”，主要目的是客户答谢，激扬士气，营造组织气氛、深化内部沟通、促进战略分享、增进目标认同，并制定目标，为新一年度的工作奏响序曲。");
//            coverContentInfoRos.add(coverContentInfoRo);
//            GoldFishCoverContentInfoRo coverContentIndfoRo = new GoldFishCoverContentInfoRo();
//            coverContentIndfoRo.setPicture("https://goldfish.zjtachao.com/banner.png");
//            coverContentIndfoRo.setText("年会指某些社会团体一年举行一次的集会，是企业和组织一年一度的“家庭盛会”，主要目的是客户答谢，激扬士气，营造组织气氛、深化内部沟通、促进战略分享、增进目标认同，并制定目标，为新一年度的工作奏响序曲。");
//            coverContentInfoRos.add(coverContentIndfoRo);
//            waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_COVER_CONTENT_INFO_KEY,JSON.toJSONString(coverContentInfoRos));

            String coverInfo = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_COVER_CONTENT_INFO_KEY);
            if(null != coverInfo && !"".equals(coverInfo)){
                List<GoldFishCoverContentInfoRo> goldFishCoverContentInfoRoList = JSON.parseArray(coverInfo,GoldFishCoverContentInfoRo.class);
                rest.setRst(goldFishCoverContentInfoRoList);
                rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                rest.setMsg("查询首页内容成功！");
            }else {
                rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                rest.setMsg("暂无首页内容！");
            }

        }catch(Exception ex){
            logger.error("查询首页内容失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("查询首页内容失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }



    /**
     * 发起抽奖（创建房间）
     */
    @GET
    @Path("/admin/build")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<String> programBuildDraw(
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token){
        WaterBootResultBean<String> rest = new WaterBootResultBean<String>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            boolean flag = true;

            if(null == token || "".equals(token)){
                flag = false;
                rest.setMsg("发起抽奖失败，token为空！");
            }
            if(flag){
                GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(token);
                if(null != goldFishUserInfoRo ){
                    boolean existsRoom = false;

                    //管理员判断
                    String roomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
                    String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
                    if(null != roomCode && !"".equals(roomCode) && null != createToken && !"".equals(createToken)){
                        existsRoom = true;
                        rest.setMsg("用户已在房间！");
                        rest.setRst(roomCode);
                        rest.setCode(3);
                    }

                    //普通用户判断
                    if(!existsRoom){
                        String normalRoomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY+token);
                        if(null != normalRoomCode  && !"".equals(normalRoomCode)){
                            existsRoom = true;
                            rest.setMsg("用户已在房间！");
                            rest.setRst(normalRoomCode);
                            rest.setCode(3);
                        }
                    }

                    if(!existsRoom) {
                        roomCode = WaterUUIDUtil.getUUID();
                        waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token,roomCode,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                        waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode,token,GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_EXPIRE_TIME);
                        //创建房间
                        GoldFishRoomInfo goldFishRoomInfo = new GoldFishRoomInfo();
                        goldFishRoomInfo.setRoomState(GoldFishCommonContext.RoomStateContext.ROOM_NOT_DRAW.getCode());
                        String roomUniqueId = GoldFishUniqueIdUtil.genrateCode();
                        goldFishRoomInfo.setRoomCode(roomCode);
                        goldFishRoomInfo.setUserCode(goldFishUserInfoRo.getUserCode());
                        goldFishRoomInfo.setRoomUniqueId(roomUniqueId);
                        goldFishRoomInfoService.createRoomInfo(goldFishRoomInfo);
                        rest.setRst(roomCode);
                        rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        rest.setMsg("发起抽奖成功！");
                    }
                }else {
                    rest.setMsg("用户未登录！");
                }
            }
        }catch(Exception ex){
            logger.error("发起抽奖失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("发起抽奖失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }

    /**
     * 扫码判断 房间是否有效
     */
    @GET
    @Path("/scan/{roomCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<Boolean> scanExpireRoom(
            @PathParam("roomCode")String roomCode,
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token){
        WaterBootResultBean<Boolean> rest = new WaterBootResultBean<Boolean>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            boolean flag = true;
            rest.setRst(false);
            if(null == roomCode || "".equals(roomCode)){
                flag = false;
                rest.setMsg("房间编码为空！");
            }
            if(flag){
                String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
                if(null != createToken && !"".equals(createToken)){
                    rest.setRst(flag);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setMsg("房间有效");
                }else {
                    rest.setMsg("房间已失效");
                }
            }
        }catch(Exception ex){
            logger.error("扫码判断房间失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("扫码判断房间失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }

    /**
     * 关闭抽奖（只有创建者才能关闭）
     */
    @GET
    @Path("/admin/close/{userCode}/{roomCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<String> programCloseDraw(
            @PathParam("userCode")String userCode,
            @PathParam("roomCode")String roomCode,
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token){
        WaterBootResultBean<String> rest = new WaterBootResultBean<String>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            Boolean flag =true ;
            if(null == userCode || "".equals(userCode)){
                flag=false;
                rest.setMsg("关闭抽奖失败，用户编码为空！");
            }

            if(flag){
                if(null == roomCode || "".equals(roomCode)){
                    flag=false;
                    rest.setMsg("关闭抽奖失败，房间编码为空！");
                }
            }

            if(flag){
                if(null == token || "".equals(token)){
                    flag=false;
                    rest.setMsg("关闭抽奖失败，token为空！");
                }
            }

            if(flag){
                //查询房间是否存在
                String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
                if(null != createToken && createToken.equals(token) ){
                    //关闭房间用户联系
                    GoldFishUserRoomRel goldFishUserRoomRel = new GoldFishUserRoomRel();
                    goldFishUserRoomRel.setRoomCode(roomCode);
                    goldFishUserRoomRelService.deleteUserRoomRel(goldFishUserRoomRel);
                    //关闭房间
                    GoldFishRoomInfo roomInfo = new GoldFishRoomInfo();
                    roomInfo.setRoomCode(roomCode);
                    goldFishRoomInfoService.deleteRoomInfo(roomInfo);
                    logger.info("创建者关闭房间！ roomCode:"+ roomCode);
                    //关闭房间  推送所有用户
                    rest.setCode(7);
                    goldFishSendMessageService.sendMessage(JSON.toJSONString(rest),roomCode,null,null);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setMsg("关闭抽奖成功！");
                    //删除二维码缓存
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_WXACODE_PNG+roomCode);
                    //关闭房间 删除房间创建者
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
//                    //关闭房间 删除缓存房间用户信息
//                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
                    //删除房间抽奖记录
                    waterRedis.delete(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode);
                }else {
                    rest.setMsg("用户不是创建者！");
                }
            }
        }catch(Exception ex){
            logger.error("关闭抽奖失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("关闭抽奖失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }



    /**
     * 发起抽奖（保存用户抽奖记录）
     */
    @POST
    @Path("/admin/start")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<GoldFishDrawRecordInfoRo> programStartDraw(
            String jsonContent,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @Context HttpServletResponse response){
        WaterBootResultBean<GoldFishDrawRecordInfoRo> rest = new WaterBootResultBean<GoldFishDrawRecordInfoRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            if(null != jsonContent && !"".equals(jsonContent) && !jsonContent.isEmpty() ){
                GoldFishDrawRecordInfo goldFishDrawRecordInfo =JSON.parseObject(jsonContent,GoldFishDrawRecordInfo.class);
                Boolean flag =true ;
                if(null == goldFishDrawRecordInfo.getRoomCode() || "".equals(goldFishDrawRecordInfo.getRoomCode())){
                    flag=false;
                    rest.setMsg("保存用户抽奖记录失败，房间编码为空！");
                }
                if(flag){
                    if(null == goldFishDrawRecordInfo.getDrawNumber()){
                        flag=false;
                        rest.setMsg("保存用户抽奖记录失败，中奖数量为空！");
                    }
                }
                if(flag){
                    if(null == goldFishDrawRecordInfo.getUserCode() || "".equals(goldFishDrawRecordInfo.getUserCode())){
                        flag=false;
                        rest.setMsg("保存用户抽奖记录失败，中奖数量为空！");
                    }
                }

                if (flag) {
                    GoldFishDrawRecordInfoRo goldFishDrawRecordInfoRo = new GoldFishDrawRecordInfoRo();
                    List<GoldFishUserInfoRo> userInfoRoList = new ArrayList<GoldFishUserInfoRo>();
                    //查询房间下所有用户
                    //调用websocket 发送抽奖通知  --给房间所有人
                    Set<String> tokenSet = waterRedis.smembers(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+goldFishDrawRecordInfo.getRoomCode());
                    if(null != tokenSet && !tokenSet.isEmpty()) {
                        //保存用户抽奖记录
                        String recordCode = WaterUUIDUtil.getUUID();
                        goldFishDrawRecordInfo.setRecordCode(recordCode);
                        goldFishDrawRecordInfo.setRecordState(GoldFishCommonContext.DrawRecordStateContext.RECORD_NORMAL.getCode());
                        goldFishDrawRecordInfoService.createDrawRecordInfo(goldFishDrawRecordInfo);
                        //随机取出用户数
                        Set<String> winUserTokenSet = new HashSet<String>();
                        while(winUserTokenSet.size() < goldFishDrawRecordInfo.getDrawNumber()){
                            String userToken =  waterRedis.srandom(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+goldFishDrawRecordInfo.getRoomCode());
                            winUserTokenSet.add(userToken);
                        }
                        for(String winUserToken :winUserTokenSet){
                            GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(winUserToken);
//                            GoldFishUserInfoSo goldFishUserInfoSo = new GoldFishUserInfoSo();
//                            goldFishUserInfoSo.setUserCode(goldFishUserInfoRo.getUserCode());
                            userInfoRoList.add(goldFishUserInfoRo);
                            //保存用户抽奖名单
                            GoldFishUserPrizeInfo goldFishUserPrizeInfo = new GoldFishUserPrizeInfo();
                            goldFishUserPrizeInfo.setUserCode(goldFishUserInfoRo.getUserCode());
                            goldFishUserPrizeInfo.setRecordCode(recordCode);
                            goldFishUserPrizeInfoService.createUserPrizeInfo(goldFishUserPrizeInfo);
                        }
                        //设置抽奖信息
                        goldFishDrawRecordInfoRo.setDrawNumber(goldFishDrawRecordInfo.getDrawNumber());
                        goldFishDrawRecordInfoRo.setRoomCode(goldFishDrawRecordInfo.getRoomCode());
                        goldFishDrawRecordInfoRo.setRecordCode(recordCode);
                        goldFishDrawRecordInfoRo.setUserInfoRos(userInfoRoList);
                        rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        rest.setRst(goldFishDrawRecordInfoRo);
                        rest.setMsg("随机抽奖成功！");
                        logger.info("发起抽奖（保存用户抽奖记录）");

                        List<GoldFishDrawRecordInfoRo> newRecordInfoRoList = new ArrayList<GoldFishDrawRecordInfoRo>();
                        goldFishDrawRecordInfoRo.setCreateTime(new Date());
                        goldFishDrawRecordInfoRo.setCreateTimeStr(WaterDateUtil.date2Str(new Date(),"yyyy-MM-dd hh:mm:ss"));
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
                        rest.setMsg("随机抽奖失败，此房间里没有用户！");
                    }
                }
            }else {
                rest.setMsg("保存用户抽奖记录失败，传递信息出错！");
            }
        }catch(Exception ex){
            logger.error("保存用户抽奖记录失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("保存用户抽奖记录失败，请稍后再试，原因：服务器出错！");
        }

        return rest;
    }
    /**
     * 发送抽奖通知
     */
    @GET
    @Path("/send/notice/{userCode}/{roomCode}/{recordCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public void programQueryPrzieList(
            @PathParam("userCode")String userCode,
            @PathParam("roomCode")String roomCode,
            @PathParam("recordCode")String recordCode){
        logger.info("调用websocket recordCode"+recordCode);
        WaterBootResultBean<GoldFishUserPrizeInfoRo> rest = new WaterBootResultBean<GoldFishUserPrizeInfoRo>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            //查询房间是是否存在
            String createToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_USER_INFO_KEY+roomCode);
            if(null != createToken){
                //查询用户用户头像
                GoldFishUserInfoRo userInfoRo = null;
                List<GoldFishUserInfoRo> goldFishUserInfoRos = null;
                String recordInfo = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY+roomCode);
                if(null != recordInfo && !"".equals(recordInfo)){
                    List<GoldFishDrawRecordInfoRo> goldFishDrawRecordInfoRos = JSON.parseArray(recordInfo,GoldFishDrawRecordInfoRo.class);
                    if(null != goldFishDrawRecordInfoRos && !goldFishDrawRecordInfoRos.isEmpty()){
                        GoldFishDrawRecordInfoRo goldFishDrawRecordInfoRo = goldFishDrawRecordInfoRos.get(0);
                        if(null != goldFishDrawRecordInfoRo && null != goldFishDrawRecordInfoRo.getUserInfoRos() && !goldFishDrawRecordInfoRo.getUserInfoRos().isEmpty()){
                            goldFishUserInfoRos =  goldFishDrawRecordInfoRo.getUserInfoRos();
                            userInfoRo = goldFishUserInfoRos.get(0);
                        }
                    }
                }
                logger.info("调用websocket 发送抽奖通知");
                //调用websocket 发送抽奖通知  --给房间所有人
                Set<String> tokenSet = waterRedis.smembers(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomCode);
                if(null != tokenSet && !tokenSet.isEmpty()) {
                    for (String userToken : tokenSet) {
                        //通过token获取用户信息
                        GoldFishUserInfoRo goldFishUserInfoRo = goldFishUserInfoService.getUserInfo(userToken);
                        if(null != goldFishUserInfoRo){
                            GoldFishUserPrizeInfoRo goldFishUserPrizeInfoRo = new GoldFishUserPrizeInfoRo();
                            goldFishUserPrizeInfoRo.setRoomCode(roomCode);
                            goldFishUserPrizeInfoRo.setUserCode(goldFishUserInfoRo.getUserCode());
                            goldFishUserPrizeInfoRo.setUserInfoRo(userInfoRo);
                            //中奖者返回中奖者头像，如果不中奖返回中奖者头像
                            int prizeState = GoldFishCommonContext.UserPrizeStateContext.NOT_WINNING.getCode();
                            for (int i = 0; i < goldFishUserInfoRos.size(); i++) {
                                if (goldFishUserInfoRo.getUserCode().equals(goldFishUserInfoRos.get(i).getUserCode())) {
                                    prizeState = GoldFishCommonContext.UserPrizeStateContext.HAVE_WINNING.getCode();
                                    goldFishUserPrizeInfoRo.setUserInfoRo(goldFishUserInfoRos.get(i));
                                    break;
                                }
                            }
                            goldFishUserPrizeInfoRo.setPrizeState(prizeState);
                            rest.setMsg("开始进行抽奖了！");
                            rest.setRst(goldFishUserPrizeInfoRo);
                            rest.setCode(15);
                            goldFishSendMessageService.sendUserMessage(JSON.toJSONString(rest), roomCode, userToken,null,null);
                        }
                    }
                }
                rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                rest.setMsg("发送抽奖通知成功！");
            }
        }catch(Exception ex){
            logger.error("发送抽奖通知失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("发送抽奖通知失败，请稍后再试，原因：服务器出错！");
        }
        logger.info(JSON.toJSONString(rest));
    }




    /**
     * 查询房间抽奖名单
     */
    @GET
    @Path("/query/prize/list/{roomCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<List<GoldFishDrawRecordInfoRo>> queryHomePrzieList(
            @PathParam("roomCode")String roomCode,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @Context HttpServletResponse response){
        WaterBootResultBean<List<GoldFishDrawRecordInfoRo>> rest = new WaterBootResultBean<List<GoldFishDrawRecordInfoRo>>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            Boolean flag =true ;
            if(flag){
                if(null == roomCode || "".equals(roomCode)){
                    flag=false;
                    rest.setMsg("查询房间抽奖名单失败，房间编码为空！");
                }
            }

            if(flag) {
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
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setRst(drawRecordInfoRos);
                    rest.setMsg("查询房间抽奖名单成功！");
                }else {
                    rest.setRst(drawRecordInfoRos);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setMsg("该房间没有中奖记录！");
                }
            }
        }catch(Exception ex){
            logger.error("查询房间抽奖名单失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("查询房间抽奖名单失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }

    /**
     * 删除抽奖记录
     */
    @GET
    @Path("/admin/delete/record/{recordCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<List<GoldFishDrawRecordInfoRo>> programDeleteRecord(
            @PathParam("recordCode")String recordCode,
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token){
        WaterBootResultBean<List<GoldFishDrawRecordInfoRo>> rest = new WaterBootResultBean<List<GoldFishDrawRecordInfoRo>>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            Boolean flag =true ;
            if(null == recordCode || "".equals(recordCode)){
                flag=false;
                rest.setMsg("删除抽奖记录失败，记录编码为空！");
            }
            if(flag){
                if(null == token || "".equals(token)){
                    flag=false;
                    rest.setMsg("删除抽奖记录失败，token为空！");
                }
            }
            if(flag) {
                String roomCode = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_USER_KEY+token);
                if(null != roomCode && !"".equals(roomCode)){
                    //删除获奖记录
                    GoldFishDrawRecordInfo drawRecordInfo = new GoldFishDrawRecordInfo();
                    drawRecordInfo.setRecordCode(recordCode);
                    goldFishDrawRecordInfoService.deleteDrawRecordInfo(drawRecordInfo);
                    //删除获奖名单
                    GoldFishUserPrizeInfo goldFishUserPrizeInfo = new GoldFishUserPrizeInfo();
                    goldFishUserPrizeInfo.setRecordCode(recordCode);
                    goldFishUserPrizeInfoService.deleteUserPrizeInfo(goldFishUserPrizeInfo);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setMsg(" 删除抽奖记录成功！");

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
                    goldFishSendMessageService.sendMessage(JSON.toJSONString(rest),roomCode,null,null);
                }else {
                    rest.setMsg("您不是抽奖发起者，无法删除");
                }

            }
        }catch(Exception ex){
            logger.error(" 删除抽奖记录失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg(" 删除抽奖记录失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }


    /**
     * 查询房间所有用户
     */
    @GET
    @Path("/query/user/list/{roomCode}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<List<GoldFishUserInfoRo>> queryRoomUserList(
            @PathParam("roomCode")String roomCode,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @Context HttpServletResponse response){
        WaterBootResultBean<List<GoldFishUserInfoRo>> rest = new WaterBootResultBean<List<GoldFishUserInfoRo>>();
        try{
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            Boolean flag =true ;
            if(null == roomCode || "".equals(roomCode)){
                flag=false;
                rest.setMsg("查询房间所有用户失败，房间编码为空！");
            }
            if(flag) {
                //查询该房间抽奖记录
                GoldFishUserRoomRelSo userRoomRelSo = new GoldFishUserRoomRelSo();
                userRoomRelSo.setRoomCode(roomCode);
                List<GoldFishUserInfoRo> goldFishUserInfoRos = goldFishUserRoomRelService.queryRoomUserList(userRoomRelSo);
                if(null != goldFishUserInfoRos && !goldFishUserInfoRos.isEmpty()){
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setRst(goldFishUserInfoRos);
                    rest.setMsg("查询房间所有用户成功!");
                }else {
                    rest.setMsg("房间暂无用户!");
                }
            }
        }catch(Exception ex){
            logger.error("查询房间所有用户失败！" , ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("查询房间所有用户失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }


//    public Set<String> get(int number,String roomcode){
//
//        Set<String> userSet =  waterRedis.srandom(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+roomcode);
//
//
//        Set<String> winUserTokenSet = new HashSet<String>();
//        while(winUserTokenSet.size() < number){
//            String userToken =  waterRedis.srandom(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_INFO_KEY+goldFishDrawRecordInfo.getRoomCode());
//            winUserTokenSet.add(userToken);
//        }
//    }
}

package com.zjtachao.fish.goldfish.data.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserInfo;
import com.zjtachao.fish.goldfish.data.pojo.domain.GoldFishUserResponseInfo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishQrcodeRo;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.pojo.so.GoldFishUserInfoSo;
import com.zjtachao.fish.goldfish.data.service.GoldFishRoomInfoService;
import com.zjtachao.fish.goldfish.data.service.GoldFishUserInfoService;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.goldfish.data.util.context.GoldFishCommonContext;
import com.zjtachao.fish.goldfish.data.util.tools.GoldFishUniqueIdUtil;
import com.zjtachao.fish.water.common.base.bean.WaterBootResultBean;
import com.zjtachao.fish.water.common.base.context.WaterBootResultContext;
import com.zjtachao.fish.water.common.base.controller.WaterBootBaseController;
import com.zjtachao.fish.water.common.tool.WaterHttpUtil;
import com.zjtachao.fish.water.common.tool.WaterUUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * 用户登录接口
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
@Controller
@Path("/login")
public class GoldFishUserLoginController extends WaterBootBaseController {

    /**
     * 用户service
     */
    @Autowired
    private GoldFishUserInfoService goldFishUserInfoService;

    /**
     * 房间service
     */
    @Autowired
    private GoldFishRoomInfoService goldFishRoomInfoService;


    /**
     * 微信小程序获取token
     */
    @POST
    @Path("/get/token")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<String> programGetToken(
            String jsonContent,
            @Context HttpHeaders httpHeaders,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @Context HttpServletResponse response) {
        WaterBootResultBean<String> rest = new WaterBootResultBean<String>();
        try {
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            if (null != jsonContent && !"".equals(jsonContent) && !jsonContent.isEmpty()) {

                String code = null;
                if (null != JSON.parseObject(jsonContent).get("code")) {
                    code = JSON.parseObject(jsonContent).get("code").toString();
                }
                String appid = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_APP_ID);
                String secret = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_APP_SECRET);
                String appUrl = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_APP_URL);
                String httpUrl = appUrl + "?appid=" + appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
                String result = WaterHttpUtil.httpGet(httpUrl);

                String openid = null;
                String unionid = null;
                JSONObject wechatResponseObj = JSON.parseObject(result);
                if (null != wechatResponseObj.get("openid")) {
                    openid = wechatResponseObj.get("openid").toString();
                    if (null != wechatResponseObj.get("unionid")) {
                        unionid = wechatResponseObj.get("unionid").toString();
                    } else {
                        unionid = wechatResponseObj.get("openid").toString();
                    }

                    String uuidkey = WaterUUIDUtil.getUUID();
                    GoldFishUserInfo waterGFUserInfo = new GoldFishUserInfo();
                    waterGFUserInfo.setUnionId(unionid);
                    waterGFUserInfo.setOpenId(openid);

                    String userKey = GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY;
                    waterRedis.set(userKey + uuidkey, JSON.toJSONString(waterGFUserInfo), GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setRst(uuidkey);
                    rest.setMsg("获取token成功！");
                } else {
                    rest.setMsg("获取token失败，原因：code出错！");
                }

            } else {
                rest.setMsg("获取token失败，传递信息出错！");
            }
        } catch (Exception ex) {
            logger.error("获取token失败！", ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("获取token失败，请稍后再试，原因：服务器出错！");
        }

        return rest;
    }


    /**
     * 微信小程序获取昵称和头像
     */
    @POST
    @Path("/get/user/info")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<GoldFishUserResponseInfo> programGetUserInfo(
            String jsonContent,
            @Context HttpHeaders httpHeaders,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @CookieParam(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY) String token,
            @Context HttpServletRequest request,
            @Context HttpServletResponse response) {
        WaterBootResultBean<GoldFishUserResponseInfo> rest = new WaterBootResultBean<GoldFishUserResponseInfo>();
        try {
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            if (null != jsonContent && !"".equals(jsonContent) && !jsonContent.isEmpty()) {
                boolean flag = true;

                GoldFishUserInfo userInfo = JSON.parseObject(jsonContent, GoldFishUserInfo.class);
                //tooken验证
                if (null == token || "".equals(token)) {
                    flag = false;
                    rest.setMsg("token为空！");
                }

                //昵称验证
                if (flag && (null == userInfo.getUserNickName() || "".equals(userInfo.getUserNickName()))) {
                    flag = false;
                    rest.setMsg("昵称为空！");
                }

                //头像验证
                if (flag && (null == userInfo.getUserImg() || "".equals(userInfo.getUserImg()))) {
                    flag = false;
                    rest.setMsg("头像为空！");
                }

                String userKey = GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY;
                Object loginInfo = waterRedis.get(userKey + token);
                if (null == loginInfo || "".equals(loginInfo)) {
                    flag = false;
                    rest.setMsg("登录失败，原因：token已过期！");
                }

                // 登录
                if (flag) {
                    GoldFishUserInfo waterGFUserInfo = JSON.parseObject(loginInfo.toString(), GoldFishUserInfo.class);
                    //查询微信用户
                    GoldFishUserInfoSo waterGFUserInfoSo = new GoldFishUserInfoSo();
                    waterGFUserInfoSo.setOpenId(waterGFUserInfo.getOpenId());
                    GoldFishUserInfoRo programUserRo = goldFishUserInfoService.querySingleUser(waterGFUserInfoSo);
                    String userCode = null;
                    String openid = null;
                    if (null != programUserRo) {
                        //修改用户登录时间与登录状态
                        userCode = programUserRo.getUserCode();
                        openid = waterGFUserInfo.getOpenId();
                        waterGFUserInfo.setUserCode(userCode);
                        waterGFUserInfo.setUserNickName(userInfo.getUserNickName());
                        waterGFUserInfo.setUserImg(userInfo.getUserImg());
                        waterGFUserInfo.setCity(userInfo.getCity());
                        waterGFUserInfo.setCountry(userInfo.getCountry());
                        waterGFUserInfo.setProvince(userInfo.getProvince());
                        waterGFUserInfo.setSex(userInfo.getSex());
                        waterGFUserInfo.setLoginTime(new Date());
                        waterGFUserInfo.setLoginType(GoldFishCommonContext.UserLoginTypeContext.PROGRAM.getCode());
                        goldFishUserInfoService.updateUser(waterGFUserInfo);
                        waterRedis.set(userKey + token, JSON.toJSONString(waterGFUserInfo), GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME);
                    } else {
                        userCode = WaterUUIDUtil.getUUID();
                        openid = waterGFUserInfo.getUnionId();
                        //新增用户
                        GoldFishUserInfo gfUserInfo = new GoldFishUserInfo();
                        gfUserInfo.setUserCode(userCode);
                        gfUserInfo.setOpenId(waterGFUserInfo.getOpenId());
                        gfUserInfo.setUnionId(openid);
                        gfUserInfo.setUserNickName(userInfo.getUserNickName());
                        gfUserInfo.setUserImg(userInfo.getUserImg());
                        gfUserInfo.setCity(userInfo.getCity());
                        gfUserInfo.setCountry(userInfo.getCountry());
                        gfUserInfo.setProvince(userInfo.getProvince());
                        gfUserInfo.setSex(userInfo.getSex());
                        gfUserInfo.setLoginTime(new Date());
                        gfUserInfo.setLoginType(GoldFishCommonContext.UserLoginTypeContext.PROGRAM.getCode());
                        goldFishUserInfoService.createUser(gfUserInfo);
                        waterRedis.set(userKey + token, JSON.toJSONString(gfUserInfo), GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME);
                    }
                    //返回userCode
                    GoldFishUserResponseInfo userResponseInfo = new GoldFishUserResponseInfo();
                    userResponseInfo.setOpenId(openid);
                    userResponseInfo.setUserCode(userCode);
                    rest.setRst(userResponseInfo);
                    rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                    rest.setMsg("登录成功！");
                }
            } else {
                rest.setMsg("登录失败，传递信息出错！");
            }
        } catch (Exception ex) {
            logger.error("登录失败！", ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("登录失败，请稍后再试，原因：服务器出错！");
        }
        return rest;
    }


    /**
     * 获得小程序二维码
     */
    @POST
    @Path("/admin/get/wxacode")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public WaterBootResultBean<String> programGetAccessToken(
            String jsonContent,
            @QueryParam("jsonpCallback") String jsonpCallback,
            @Context HttpServletResponse response) {
        WaterBootResultBean<String> rest = new WaterBootResultBean<String>();
        try {
            rest.setCode(WaterBootResultContext.ResultCode.VALID_NO_PASS.getCode());
            logger.info("生成二维码："+jsonContent);
            if (null != jsonContent && !"".equals(jsonContent) && !jsonContent.isEmpty()) {
                //房间关闭的情况下，不能生成二维码
                GoldFishQrcodeRo qrcodeRo = JSON.parseObject(jsonContent , GoldFishQrcodeRo.class);
                if (null != qrcodeRo && null != qrcodeRo.getScene()) {
                    String roomCode = qrcodeRo.getScene();
                    logger.info("生成二维码时传的roomCode: " + roomCode);
                    String wxcodeUrl = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_WXACODE_PNG + roomCode);
                    if(null != wxcodeUrl && !"".equals(wxcodeUrl)){
                        rest.setRst(wxcodeUrl);
                        rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                        rest.setMsg("生成二维码成功！");
                    }else {
                        String accessToken = getAccessToken();
                        if(null != accessToken && !"".equals(accessToken)){
                            String serverPicUrl = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_SERVER_URL);
                            String httpPicUrl = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_HTTP_URL);
                            String codeUrl = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_GET_WXACODE_URL);
                            String postUrl = codeUrl + accessToken;
                            String name = GoldFishUniqueIdUtil.genrateCode();
                            WaterHttpUtil.httpPostForImg(postUrl, jsonContent, serverPicUrl + name + ".png");
                            rest.setRst(httpPicUrl + name + ".png");
                            rest.setCode(WaterBootResultContext.ResultCode.SUCCESS.getCode());
                            rest.setMsg("生成二维码成功！");
                            //二维码存缓存
                            waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ROOM_WXACODE_PNG + roomCode, httpPicUrl + name + ".png",60*60*24);
                        } else {
                            rest.setMsg("生成二维码失败，accessToken出错！");
                        }
                    }
                } else {
                    rest.setMsg("生成二维码失败，scene为空！");
                }
            }

        } catch (Exception ex) {
            logger.error("生成二维码失败！", ex);
            rest.setCode(WaterBootResultContext.ResultCode.ERROR.getCode());
            rest.setMsg("生成二维码失败，请稍后再试，原因：服务器出错！");
        }

        return rest;
    }


    /**
     * 获取accessToken
     * @return
     */
    private String getAccessToken(){

        String accessToken = waterRedis.get(GoldFishCommonConstant.WATER_PRGRAM_GF_ACCESS_TOKEN_KEY);
        if(null == accessToken || "".equals(accessToken)){
            String appid = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_APP_ID);
            String secret = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_APP_SECRET);
            String url = waterEnv.getProperty(GoldFishCommonConstant.WATER_PRGRAM_GF_ACCESS_TOKEN_URL);
            String grant_type = "grant_type=client_credential";
            String appId = "&appid=" + appid;
            String appsecret = "&secret=" + secret;
            String getUrl = url + grant_type + appId + appsecret;
            String result = WaterHttpUtil.httpGet(getUrl);
            if (result.contains("access_token")) {
                JSONObject wechatResponseObj = JSON.parseObject(result);
                if (null != wechatResponseObj.get("access_token")) {
                    accessToken = wechatResponseObj.get("access_token").toString();
                    waterRedis.set(GoldFishCommonConstant.WATER_PRGRAM_GF_ACCESS_TOKEN_KEY,accessToken,3000);
                }

            }
        }
        return accessToken;
    }
}

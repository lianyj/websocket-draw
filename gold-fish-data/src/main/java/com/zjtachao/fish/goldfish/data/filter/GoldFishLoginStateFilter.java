/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.filter;

import com.alibaba.fastjson.JSON;
import com.zjtachao.fish.goldfish.data.pojo.ro.GoldFishUserInfoRo;
import com.zjtachao.fish.goldfish.data.util.constant.GoldFishCommonConstant;
import com.zjtachao.fish.goldfish.data.util.context.GoldFishCommonContext;
import com.zjtachao.fish.water.common.data.WaterRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.Map;

/**
 * 用户登录状态拦截
 *
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @since 2.0
 */
@Provider
@Priority(Priorities.AUTHENTICATION)
public class GoldFishLoginStateFilter implements ContainerRequestFilter {

    /** 日志 **/
    public static final Logger logger = LoggerFactory.getLogger(GoldFishLoginStateFilter.class);

    /** Redis **/
    @Autowired
    public WaterRedis waterRedis;

    /**
     *
     * @param requestContext
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {
        try{
            String url = requestContext.getUriInfo().getPath();
            if(url.contains("/admin")){
                boolean flag = false;
                Map<String , Cookie> cookieMap = requestContext.getCookies();
                if(null != cookieMap && cookieMap.containsKey(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY)){
                    Cookie loginCookie = cookieMap.get(GoldFishCommonConstant.WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY);
                    if(null != loginCookie && null != loginCookie.getValue() && !"".equals(loginCookie.getValue())){
                        String userJson = waterRedis.queryString(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY+loginCookie.getValue());
                        Long expireTime = waterRedis.ttl(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY+loginCookie.getValue());
                        if(null != userJson && !"".equals(userJson)){
                            GoldFishUserInfoRo userRo = JSON.parseObject(userJson , GoldFishUserInfoRo.class);
                            if(null != userRo){
                                flag = true;
                                //延期登录
                                if(null != userRo.getLoginType()){
                                    if(userRo.getLoginType().equals(GoldFishCommonContext.UserLoginTypeContext.PROGRAM.getCode())){
                                        if((GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME - expireTime)
                                                > GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME / 3){
                                            waterRedis.expireTime(GoldFishCommonConstant.WATER_PRGRAM_GF_USER_INFO_KEY+loginCookie.getValue()
                                                    ,GoldFishCommonConstant.WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME/60);
                                        }
                                    }
                                }else if(null != userRo.getLoginType()){
                                    if(userRo.getLoginType().equals(GoldFishCommonContext.UserLoginTypeContext.MOBILE.getCode())){
                                        if((GoldFishCommonConstant.WATER_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME - expireTime)
                                                > GoldFishCommonConstant.WATER_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME / 3){
                                            waterRedis.expireTime(GoldFishCommonConstant.WATER_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME+loginCookie.getValue()
                                                    ,GoldFishCommonConstant.WATER_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME/60);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if(!flag){
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"code\":1,\"msg\":\"no login\"}").build());
                }
            }

        }catch (Exception ex){
            logger.error("验证用户是否登录出错！" , ex);
        }

    }
}

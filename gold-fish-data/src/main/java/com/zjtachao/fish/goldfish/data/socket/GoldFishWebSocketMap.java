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



import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * websocket保存的map
 *
 * @author <a href="mailto:dh@zjtachao.com">lyj</a>
 * @since 2.0
 */
public class GoldFishWebSocketMap {

    /** websocket连接  sessionid   */
    private static Map<String, GoldFishWebSocket> sessionMap = new ConcurrentHashMap<String, GoldFishWebSocket>();

    /** tooken用户 */
    private static Map<String, String> idMap = new ConcurrentHashMap<String, String>();

    public static void addSession(String sessionid, GoldFishWebSocket goldFishWebSocketCntroller){
        sessionMap.put(sessionid,goldFishWebSocketCntroller);
    }
    public static GoldFishWebSocket getSession(String sessionid){
        return sessionMap.get(sessionid);
    }

    public static void removeSession(String sessionid){
        sessionMap.remove(sessionid);
    }

    public static boolean containSession(String sessionid){
        return sessionMap.containsKey(sessionid);
    }

    public static void addId(String sessionid,String token){
        idMap.put(sessionid,token);
    }
    public static String getId(String sessionid){
        return idMap.get(sessionid);
    }

    public static void removeId(String sessionid){
        idMap.remove(sessionid);
    }

    public static boolean containId(String sessionid){
        return idMap.containsKey(sessionid);
    }



}

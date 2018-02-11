package com.zjtachao.fish.goldfish.data.util.constant;

/**
 * 常量类
 * @author <a href="mailto:zgf@zjtachao.com">zhuguofeng</a>
 * @since 2.0
 */
public class GoldFishCommonConstant {

    /**  water 金鱼项目 accessToken 的key **/
    public static final String WATER_PRGRAM_GF_ACCESS_TOKEN_KEY  = "water:program:goldfish:access.token";

    /** water 金鱼项目 用户登录cookie的key **/
    public static final String WATER_PRGRAM_GF_LOGIN_USER_COOKIE_KEY = "GFUID";

    /** water用户信息 **/
    public static final String WATER_PRGRAM_GF_USER_INFO_KEY  = "water:program:goldfish:user:info.token.";

    /** 房间过期时间 **/
    public static final long WATER_PRGRAM_GF_ROOM_EXPIRE_TIME = 60 * 60;

    /** 普通登录过期时间 **/
    public static final long WATER_COMMON_USER_NORMAL_LOGIN_EXPIRE_TIME = 60 * 60 * 12;

    /** 微信小程序登录过期时间 **/
    public static final long WATER_COMMON_USER_PROGRAM_LOGIN_EXPIRE_TIME = 60 * 60 * 24 * 30;

    /** water 金鱼项目app id **/
    public static final String WATER_PRGRAM_GF_APP_ID  = "com.zjtachao.water.goldfish.program.app.id";

    /** water金鱼项目app secret **/
    public static final String WATER_PRGRAM_GF_APP_SECRET  = "com.zjtachao.water.goldfish.program.app.secret";

    /** water金鱼项目用户登录app url **/
    public static final String WATER_PRGRAM_GF_APP_URL  = "com.zjtachao.water.goldfish.program.app.url";

    /** water金鱼项目获取access_token url **/
    public static final String WATER_PRGRAM_GF_ACCESS_TOKEN_URL  = "com.zjtachao.water.goldfish.program.access.token.url";

    /** water金鱼项目获取小程序码 url **/
    public static final String WATER_PRGRAM_GF_GET_WXACODE_URL  = "com.zjtachao.water.goldfish.program.get.wxacode.url";

    /** water金鱼项目获取小程序码 服务器url **/
    public static final String WATER_PRGRAM_GF_SERVER_URL  = "com.zjtachao.water.goldfish.program.server.path";

    /** water金鱼项目获取小程序码 网络url **/
    public static final String WATER_PRGRAM_GF_HTTP_URL  = "com.zjtachao.water.goldfish.program.http.path";

    /** water金鱼项目 http 前缀 **/
    public static final String WATER_PRGRAM_GF_HTTP_PREFIX  = "com.zjtachao.water.goldfish.program.http.prefix";

    /** 获得服务器地址  **/
    public static final String WATER_PRGRAM_GF_LOCAL_SERVER_URL = "goldfish.websocket.address";

    /** 存放发送消息的接口地址 **/
    public static final String WATER_PRGRAM_GF_SEND_MESSAGE_REST_URL = "goldfish.websocket.message.rest";

//    /** 存放房间里用户的编码 **/
//    public static final String WATER_PRGRAM_GF_ROOM_USERCODE_SET = "goldfish:program:room:userCode:set.";

    /** 存放用户的编码里的地址 **/
    public static final String WATER_PRGRAM_GF_USER_ADDRESS = "goldfish:program:user:address.";

    /** 存放房间二维码图片的地址 **/
    public static final String WATER_PRGRAM_GF_ROOM_WXACODE_PNG = "goldfish:program:room:wxacode:png.";

    /** 存放房间信息 redis **/
    public static final String WATER_PRGRAM_GF_ROOM_INFO_KEY = "goldfish:program:room:info.";

    /** 存放房间 --管理员 redis **/
    public static final String WATER_PRGRAM_GF_ROOM_USER_INFO_KEY = "goldfish:program:room:create:usercode.";

    /** 存放管理员--房间  redis **/
    public static final String WATER_PRGRAM_GF_ROOM_INFO_USER_KEY = "goldfish:program:room:admin:user:info.";

    /** 存放房间下中奖名单 redis **/
    public static final String WATER_PRGRAM_GF_ROOM_RECORD_INFO_KEY = "goldfish:program:room:record:info.";

    /** 存放普通用户--房间  redis **/
    public static final String WATER_PRGRAM_GF_ROOM_COMMON_USER_KEY = "goldfish:program:room:common:user:info.";

    /** 存放封面图片文字消息  redis **/
    public static final String WATER_PRGRAM_GF_COVER_CONTENT_INFO_KEY = "goldfish:program:cover:content:info";
}

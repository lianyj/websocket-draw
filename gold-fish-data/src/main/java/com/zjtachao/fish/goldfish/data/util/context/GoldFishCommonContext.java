package com.zjtachao.fish.goldfish.data.util.context;

/**
 * 枚举类
 * @author <a href="mailto:zgf@zjtachao.com">zhuguofeng</a>
 * @since 2.0
 */
public class GoldFishCommonContext {

    /**
     * 删除标志枚举
     * @author <a href="mailto:zgf@zjtachao.com">zhuguofeng</a>
     * @version $Id$
     * @since 2.0
     */
    public enum DeleteFlagContext{
        /** 正常 **/
        DELETE_NO("0" , "否"),
        /** 已删除 **/
        DELETE_YES("1" , "是");

        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private DeleteFlagContext(String code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (DeleteFlagContext c : DeleteFlagContext.values()) {
                if (c.getCode().equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     *
     * 用户登录类型
     * @author <a href="mailto:lyj@zjtachao.com">lyj</a>
     * @version $Id$
     * @since 2.0
     */
    public enum UserLoginTypeContext{
        /** 手机**/
        MOBILE("MOBILE" ,"手机"),
        /** 邮箱 **/
        EMAIL("EMAIL" ,"邮箱"),
        /** QQ**/
        QQ("QQ" ,"QQ"),
        /** 微信 **/
        WECHAT("WECHAT" ,"微信"),
        /** 小程序 **/
        PROGRAM("PROGRAM" ,"小程序"),
        /** 网页 **/
        WEB("WEB" ,"网页"),
        /** 验证码 **/
        SMS("SMS" ,"验证码");


        /** 编码 **/
        private String code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private UserLoginTypeContext(String code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(String code) {
            String name = null;
            for (UserLoginTypeContext c : UserLoginTypeContext.values()) {
                if (c.code.equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * 房间状态枚举
     * @author <a href="mailto:lyj@zjtachao.com">lyj</a>
     * @version $Id$
     * @since 2.0
     */
    public enum RoomStateContext{
        /** 未抽奖**/
        ROOM_NOT_DRAW(1 , "未开始抽奖"),
        /** 抽奖进行中 **/
        ROOM_DRAWING(2 , "抽奖进行中"),
        /** 已关闭 **/
        ROOM_DRAW_CLOSED(3 , "抽奖已结束");

        /** 编码 **/
        private Integer code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private RoomStateContext(Integer code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(Integer code) {
            String name = null;
            for (RoomStateContext c : RoomStateContext.values()) {
                if (c.getCode().equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public Integer getCode() {
            return code;
        }
        public void setCode(Integer code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * 抽奖记录状态枚举
     * @author <a href="mailto:lyj@zjtachao.com">lyj</a>
     * @version $Id$
     * @since 2.0
     */
    public enum DrawRecordStateContext{
        /** 正常 **/
        RECORD_NORMAL(1 , "正常"),
        /** 作废 **/
        RECORD_CANCEL(2 , "作废");

        /** 编码 **/
        private Integer code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private DrawRecordStateContext(Integer code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(Integer code) {
            String name = null;
            for (DrawRecordStateContext c : DrawRecordStateContext.values()) {
                if (c.getCode().equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public Integer getCode() {
            return code;
        }
        public void setCode(Integer code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }


    /**
     * 用户中奖状态枚举
     * @author <a href="mailto:lyj@zjtachao.com">lyj</a>
     * @version $Id$
     * @since 2.0
     */
    public enum UserPrizeStateContext{
        /** 已中奖 **/
        HAVE_WINNING(1 , "已中奖"),
        /** 未中奖 **/
        NOT_WINNING(2 , "未中奖");

        /** 编码 **/
        private Integer code;
        /** 名称 **/
        private String name;

        /**
         * 构造方法
         *@param name name
         *@param code code
         */
        private UserPrizeStateContext(Integer code, String name) {
            this.name = name;
            this.code = code;
        }

        /**
         *
         * 获得名称
         * @param code code
         * @return String 名称
         */
        public static String getName(Integer code) {
            String name = null;
            for (UserPrizeStateContext c : UserPrizeStateContext.values()) {
                if (c.getCode().equals(code)) {
                    name = c.getName();
                }
            }
            return name;
        }
        public Integer getCode() {
            return code;
        }
        public void setCode(Integer code) {
            this.code = code;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}

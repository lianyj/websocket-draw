/**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 *
 * 项目名称：浙江踏潮-基础架构
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.pojo.so;

import com.zjtachao.fish.water.common.base.bean.WaterBootBaseSo;
import com.zjtachao.fish.water.common.base.context.WaterBootCommonContext;

/**
 * 用户中奖名单so
 *
 * @author <a href="mailto:LYJ@zjtachao.com">LYJ</a>
 * @since 2.0
 */
public class GoldFishUserPrizeInfoSo extends WaterBootBaseSo {

    private static final long serialVersionUID = 8633395132940748467L;
    /** 主键id */
    private Long id;

    /** 记录编码 */
    private String recordCode;

    /** 用户编码 */
    private String userCode;

    public String deleteFlag = WaterBootCommonContext.DeleteFlagContext.DELETE_NO.getCode();

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}

 /**************************************************************************
 * Copyright (c) 2016-2017 Zhejiang TaChao Network Technology Co.,Ltd.
 * All rights reserved.
 * 
 * 项目名称：浙江踏潮
 * 版权说明：本软件属浙江踏潮网络科技有限公司所有，在未获得浙江踏潮网络科技有限公司正式授权
 *           情况下，任何企业和个人，不能获取、阅读、安装、传播本软件涉及的任何受知
 *           识产权保护的内容。                            
 ***************************************************************************/
package com.zjtachao.fish.goldfish.data.util.tools;

import com.zjtachao.fish.water.common.tool.WaterDateUtil;
import com.zjtachao.fish.water.common.tool.WaterRandomUtil;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


 /**
 * 唯一编码处理
 * @author <a href="mailto:dh@zjtachao.com">duhao</a>
 * @version $Id$   
 * @since 2.0
 */

public class GoldFishUniqueIdUtil {
	
	/** 单个应用计数器 **/
	private static final AtomicInteger CODE_COUNT = new AtomicInteger(1);

	/**
	 * 
	   * 生成编码
	   * @return
	 */
	public static String genrateCode(){
		StringBuffer code = new StringBuffer();
		//时间
		String dateStr = WaterDateUtil.date2Str(new Date(), "yyMMddHHmmssSSS");
		code.append(dateStr);
		
		//计数器2位
		int count = CODE_COUNT.getAndAdd(1);
		if(count > 99){
			count = 1;
			CODE_COUNT.set(1);
		}
		if(count<10){
			code.append("0");
		}
		code.append(count);
		
		//随机数3位
		String ranStr = WaterRandomUtil.random(3);
		code.append(ranStr);
		return code.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(genrateCode());
	}

}

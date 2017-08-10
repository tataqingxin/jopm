/** 
 * @Description:[验证码生成器]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.captcha.entity.RandCodeImageEnum.java
 * @ClassName:RandCodeImageEnum
 * @Author:Lu Guoqiang  
 * @CreateDate:2016年5月15日 上午2:23:05
 * @UpdateUser:Lu Guoqiang   
 * @UpdateDate:2016年5月15日 上午2:23:05  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.captcha.entity;

import java.util.Random;

/**
 * @ClassName: RandCodeImageEnum 
 * @Description: [验证码生成器] 
 * @author Lu Guoqiang 
 * @date 2016年5月15日 上午2:23:05 
 * @since JDK 1.6 
 */
public enum RandCodeImageEnum {
	 /**
     * 混合字符串
     */
    ALL_CHAR("0123456789abcdefghijkmnpqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"), // 去除小写的l和o这个两个不容易区分的字符；
    /**
     * 字符
     */
    LETTER_CHAR("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
    /**
     * 小写字母
     */
    LOWER_CHAR("abcdefghijklmnopqrstuvwxyz"),
    /**
     * 数字
     */
    NUMBER_CHAR("0123456789"),
    /**
     * 大写字符
     */
    UPPER_CHAR("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    
    /**
     * 待生成的字符串
     */
    private String charStr;

    /**
     * @param charStr
     */
    private RandCodeImageEnum(final String charStr) {
        this.charStr = charStr;
    }

    /**
     * 生产随机验证码
     * @param codeLength 验证码的长度
     * @return 验证码
     */
    public String generateStr(final int codeLength) {
        final StringBuffer sb = new StringBuffer();
        final Random random = new Random();
        final String sourseStr = getCharStr();

        for (int i = 0; i < codeLength; i++) {
            sb.append(sourseStr.charAt(random.nextInt(sourseStr.length())));
        }

        return sb.toString();
    }

    /**
     * @return the {@link #charStr}
     */
    public String getCharStr() {
        return charStr;
    }
}

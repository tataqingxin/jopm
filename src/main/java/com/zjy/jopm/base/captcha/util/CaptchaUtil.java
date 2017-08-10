/** 
 * @Description:[验证码工具类]   
 * @ProjectName:jump 
 * @Package:com.zjy.jopm.base.captcha.util.CaptchaUtil.java
 * @ClassName:CaptchaUtil
 * @Author: Lu Guoqiang 
 * @CreateDate:2016年5月15日 上午2:27:07
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月15日 上午2:27:07  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.captcha.util;

import java.awt.Color;
import java.util.Random;

import com.unistc.utils.ResourceUtil;
import com.zjy.jopm.base.captcha.entity.RandCodeImageEnum;

/**
 * @ClassName: CaptchaUtil 
 * @Description: [验证码工具类] 
 * @author Lu Guoqiang 
 * @date 2016年5月15日 上午2:27:07 
 * @since JDK 1.6 
 */
public class CaptchaUtil {
	
    /**
    * 干扰线程度
    */
   public static final int count = 200;

   /**
    * 定义图形大小
    */
   public static final int width = 105;
   /**
    * 定义图形大小
    */
   public static final int height = 35;
   
   // private Font mFont = new Font("Arial Black", Font.PLAIN, 15); //设置字体
   /**
    * 干扰线的长度=1.414*lineWidth
    */
   public static final int lineWidth = 2;

    /**
     * 
     * @Title: exctractRandCode 
     * @Description: [生成指定长度及类型的验证码]
     * @return
     */
    public static final String exctractRandCode() {
    	//获取随机码的类型
        final String randCodeType = ResourceUtil.getConfigByName("randCodeType");
        //获取随机码的长度
        int randCodeLength = Integer.parseInt(ResourceUtil.getConfigByName("randCodeLength"));
        if (randCodeType == null) {
            return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
        } else {
            switch (randCodeType.charAt(0)) {
                case '1':
                    return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
                case '2':
                    return RandCodeImageEnum.LOWER_CHAR.generateStr(randCodeLength);
                case '3':
                    return RandCodeImageEnum.UPPER_CHAR.generateStr(randCodeLength);
                case '4':
                    return RandCodeImageEnum.LETTER_CHAR.generateStr(randCodeLength);
                case '5':
                    return RandCodeImageEnum.ALL_CHAR.generateStr(randCodeLength);

                default:
                    return RandCodeImageEnum.NUMBER_CHAR.generateStr(randCodeLength);
            }
        }
    }

    /**
     * 随机上色
     * @param fc
     * @param bc
     * @return
     */
    public static final Color getRandColor(int fc, int bc) { // 取得给定范围随机颜色
        final Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }

        final int r = fc + random.nextInt(bc - fc);
        final int g = fc + random.nextInt(bc - fc);
        final int b = fc + random.nextInt(bc - fc);

        return new Color(r, g, b);
    }
}

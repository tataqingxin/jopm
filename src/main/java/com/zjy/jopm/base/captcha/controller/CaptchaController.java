/** 
 * @Description:[验证码]   
 * @ProjectName:jopm 
 * @Package:com.zjy.jopm.base.captcha.controller.CaptchaController.java
 * @ClassName:CaptchaController
 * @Author:Lu Guoqiang
 * @CreateDate:2016年5月9日 下午2:41:47
 * @UpdateUser:Lu Guoqiang  
 * @UpdateDate:2016年5月9日 下午2:41:47  
 * @UpdateRemark:[说明本次修改内容]
 * @Version:V1.0 
 * @Copyright (c) 2016, 北京紫金云教育科技有限公司  All Rights Reserved. 
 * 
 */  
package com.zjy.jopm.base.captcha.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.unistc.exception.JumpException;
import com.zjy.jopm.base.captcha.util.CaptchaUtil;
import com.zjy.jopm.base.common.Constants;

/**
 * 
 * @ClassName: CaptchaController 
 * @Description: [验证码] 
 * @author Lu Guoqiang
 * @date 2016年5月15日 上午2:08:13 
 * @since JDK 1.6
 */
@Controller
@RequestMapping("/captchaController")
public class CaptchaController {

    

    /**
     * 随机生成验证图片
     * @param request
     * @param response
     */
    @RequestMapping(value = "/captcha")
    public void getCaptcha(final HttpServletRequest request, final HttpServletResponse response){
        // 设置页面不缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 在内存中创建图象
        final BufferedImage image = new BufferedImage(CaptchaUtil.width, CaptchaUtil.height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        final Graphics2D graphics = (Graphics2D) image.getGraphics();

        // 设定背景颜色
        graphics.setColor(Color.WHITE); // ---1
        graphics.fillRect(0, 0, CaptchaUtil.width, CaptchaUtil.height);
        // 设定边框颜色
        graphics.drawRect(0, 0, CaptchaUtil.width - 1, CaptchaUtil.height - 1);

        final Random random = new Random();
        // 随机产生干扰线，使图象中的认证码不易被其它程序探测到
        for (int i = 0; i < CaptchaUtil.count; i++) {
            graphics.setColor(CaptchaUtil.getRandColor(150, 200)); // ---3

            final int x = random.nextInt(CaptchaUtil.width - CaptchaUtil.lineWidth - 1) + 1; // 保证画在边框之内
            final int y = random.nextInt(CaptchaUtil.height - CaptchaUtil.lineWidth - 1) + 1;
            final int xl = random.nextInt(CaptchaUtil.lineWidth);
            final int yl = random.nextInt(CaptchaUtil.lineWidth);
            graphics.drawLine(x, y, x + xl, y + yl);
        }

        // 取随机产生的认证码(4位数字)
        final String resultCode = CaptchaUtil.exctractRandCode();
        for (int i = 0; i < resultCode.length(); i++) {
            // 设置字体颜色
            graphics.setColor(Color.BLACK);
            // 设置字体样式
            graphics.setFont(new Font("Times New Roman", Font.BOLD, 24));
            // 设置字符，字符间距，上边距
            graphics.drawString(String.valueOf(resultCode.charAt(i)), (23 * i) + 8, 26);
        }

        // 将认证码存入SESSION
        request.getSession().setAttribute(Constants.SESSION_KEY_OF_RAND_CODE, resultCode);
        // 图象生效
        graphics.dispose();

        // 输出图象到页面
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            throw new JumpException(e);
        }
    }


}

package com.ent.happychat.common.tools;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

/**
 * 生产工具
 */
@Slf4j
public class GenerateTools {
    /**
     * 根据当前时间生成编号
     *
     * @return
     */
    public static String getTimeNo() {
        return LocalDateTime.now().toString()
                .replace(".", "")
                .replace("T", "")
                .replace(":", "")
                .replace("-", "");
    }

    /**
     * 生成五位验证码
     */
    public static String getVerificationCode() {
        int num = RandomUtil.randomInt(10000, 99999);
        return String.valueOf(num);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 创建tokenId
     *
     * @return
     */
    public static String createTokenId() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 随机出来几位验证码值
     * @param captchaLength
     * @return
     */
    public static String getCaptchaText(int captchaLength) {
        String captchaChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captchaText = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < captchaLength; i++) {
            int index = random.nextInt(captchaChars.length());
            captchaText.append(captchaChars.charAt(index));
        }

        return captchaText.toString();
    }

    /**
     * 生成验证码图片
     * @param captchaText
     * @return
     * @throws IOException
     */
    public static String getCaptchaImage(String captchaText) throws IOException {
        int width = 160;
        int height = 40;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        Random random = new Random();

        // 填充背景
        graphics.setColor(new Color(240, 240, 240));
        graphics.fillRect(0, 0, width, height);

        // 绘制干扰线
        graphics.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            graphics.drawLine(x, y, x2, y2);
        }

        // 绘制验证码字符
        graphics.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < captchaText.length(); i++) {
            graphics.setColor(new Color(random.nextInt(128), random.nextInt(128), random.nextInt(128)));
            graphics.drawString(String.valueOf(captchaText.charAt(i)), 20 + i * 20, 30);
        }

        graphics.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }


}

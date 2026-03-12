package com.loafer.blog.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AvatarGenerator {

    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final String[] COLORS = {
            "#FF6B6B", "#4ECDC4", "#45B7D1", "#96CEB4", "#FFEAA7",
            "#DDA0DD", "#98D8C8", "#F7DC6F", "#BB8FCE", "#85C1E9"
    };

    /**
     * 生成默认头像
     * @param username 用户名
     * @param outputPath 输出路径
     * @return 生成的头像文件名
     * @throws IOException IO异常
     */
    public static String generateAvatar(String username, String outputPath) throws IOException {
        // 确保输出目录存在
        File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成随机背景颜色
        Random random = new Random();
        String bgColorHex = COLORS[random.nextInt(COLORS.length)];
        Color bgColor = hexToColor(bgColorHex);

        // 生成对比色作为字体颜色
        Color fontColor = getContrastColor(bgColor);

        // 创建图片
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // 填充背景
        g2d.setColor(bgColor);
        g2d.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置字体
        Font font = new Font("Arial", Font.BOLD, 80);
        g2d.setFont(font);
        g2d.setColor(fontColor);

        // 获取用户名首字母
        char initial = getInitial(username);

        // 计算文字位置
        FontMetrics metrics = g2d.getFontMetrics(font);
        int x = (WIDTH - metrics.stringWidth(String.valueOf(initial))) / 2;
        int y = (HEIGHT - metrics.getHeight()) / 2 + metrics.getAscent();

        // 绘制文字
        g2d.drawString(String.valueOf(initial), x, y);
        g2d.dispose();

        // 生成文件名
        String fileName = "avatar_" + System.currentTimeMillis() + ".png";
        File outputFile = new File(outputPath, fileName);

        // 保存图片
        ImageIO.write(image, "png", outputFile);

        return fileName;
    }

    /**
     * 将十六进制颜色转换为 Color 对象
     * @param hex 十六进制颜色值
     * @return Color 对象
     */
    private static Color hexToColor(String hex) {
        return new Color(
                Integer.parseInt(hex.substring(1, 3), 16),
                Integer.parseInt(hex.substring(3, 5), 16),
                Integer.parseInt(hex.substring(5, 7), 16)
        );
    }

    /**
     * 获取与背景色对比的字体颜色
     * @param bgColor 背景颜色
     * @return 对比字体颜色
     */
    private static Color getContrastColor(Color bgColor) {
        int r = bgColor.getRed();
        int g = bgColor.getGreen();
        int b = bgColor.getBlue();
        int brightness = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        return brightness > 128 ? Color.BLACK : Color.WHITE;
    }

    /**
     * 获取用户名首字母
     * @param username 用户名
     * @return 首字母
     */
    private static char getInitial(String username) {
        if (username == null || username.isEmpty()) {
            return 'U';
        }
        return Character.toUpperCase(username.charAt(0));
    }
}

package com.loafer.blog.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.IOException;
import java.util.Iterator;

/**
 * 文件上传工具类
 * @author loafer
 * @since 2026-03-12 20:41:54
 **/
public class FileUploadUtils {

    // 判断请求是否包含文件上传
    public static boolean hasFileUpload(HttpServletRequest request, MultipartResolver multipartResolver) {
        // 第一步：判断请求是否为 multipart 类型
        if (!multipartResolver.isMultipart(request)) {
            return false;
        }

        // 第二步：转换为 MultipartRequest，检查是否有文件项
        MultipartRequest multipartRequest = multipartResolver.resolveMultipart(request);
        Iterator<String> fileNames = multipartRequest.getFileNames();
        while (fileNames.hasNext()) {
            String fileName = fileNames.next();
            MultipartFile file = multipartRequest.getFile(fileName);
            // 排除空文件（文件字段存在但未上传内容）
            if (file != null && !file.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // 获取文件类型（方式1：基于请求头 Content-Type）
    public static String getFileTypeFromHeader(MultipartFile file) {
        return file.getContentType();
    }

    // 获取文件类型（方式2：解析文件字节，真实类型，推荐）
    public static String getRealFileType(MultipartFile file) throws IOException {
        // 读取文件前几个字节，判断文件魔数（Magic Number）
        byte[] header = new byte[8];
        file.getInputStream().read(header);
        String fileType = getFileTypeByMagicNumber(header);
        // 若魔数解析失败，降级使用请求头类型
        return fileType != null ? fileType : file.getContentType();
    }

    // 解析文件魔数，判断真实类型
    private static String getFileTypeByMagicNumber(byte[] header) {
        if (header == null || header.length < 4) {
            return null;
        }

        // 常见文件魔数匹配（可扩展）
        String hex = bytesToHex(header).toUpperCase();
        if (hex.startsWith("89504E47")) {
            // PNG
            return MediaType.IMAGE_PNG_VALUE;
        } else if (hex.startsWith("FFD8FF")) {
            // JPG/JPEG
            return MediaType.IMAGE_JPEG_VALUE;
        } else if (hex.startsWith("25504446")) {
            // PDF
            return "application/pdf";
        } else if (hex.startsWith("504B0304")) {
            // ZIP
            return "application/zip";
        }
        return null;
    }

    // 字节数组转十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
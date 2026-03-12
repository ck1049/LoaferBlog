package com.loafer.blog.controller;

import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.utils.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

    // 文件存储路径
    @Value("${file.upload.dir}")
    private String UPLOAD_DIR;
    
    @Value("${file.access.prefix}")
    private String ACCESS_PREFIX;

    // 确保上传目录存在
    private void ensureDirExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseVO<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseVO.error("文件不能为空");
            }

            // 确保上传目录存在
            ensureDirExists(UPLOAD_DIR);

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path path = Paths.get(UPLOAD_DIR, filename);
            Files.write(path, file.getBytes());

            // 生成访问URL
            String fileUrl = FileUploadUtils.spliceUrl(ACCESS_PREFIX + "/" + filename);

            // 返回文件信息
            return ResponseVO.success(java.util.Map.of("url", fileUrl));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseVO.error("上传失败: " + e.getMessage());
        }
    }

    /**
     * 上传Markdown文件
     */
    @PostMapping("/upload-markdown")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseVO<?> uploadMarkdownFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseVO.error("文件不能为空");
            }

            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.endsWith(".md")) {
                return ResponseVO.error("只能上传Markdown文件");
            }

            // 确保上传目录存在
            ensureDirExists(UPLOAD_DIR);

            // 生成唯一文件名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filename = UUID.randomUUID().toString() + extension;

            // 保存文件
            Path path = Paths.get(UPLOAD_DIR, filename);
            Files.write(path, file.getBytes());

            // 生成访问URL
            String fileUrl = FileUploadUtils.spliceUrl(ACCESS_PREFIX + "/" + filename);

            // 读取文件内容
            String content = new String(file.getBytes());

            // 返回文件内容和URL
            return ResponseVO.success(java.util.Map.of("content", content, "url", fileUrl));
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseVO.error("上传失败: " + e.getMessage());
        }
    }
}

package com.loafer.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
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
            String fileUrl = ACCESS_PREFIX + "/" + filename;

            // 返回文件信息
            return ResponseEntity.ok()
                    .body("{\"code\": 200, \"message\": \"上传成功\", \"data\": {\"url\": \"" + fileUrl + "\"}}" );
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 500, \"message\": \"上传失败: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 上传Markdown文件
     */
    @PostMapping("/upload-markdown")
    public ResponseEntity<?> uploadMarkdownFile(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }

            // 检查文件类型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !originalFilename.endsWith(".md")) {
                return ResponseEntity.badRequest().body("只能上传Markdown文件");
            }

            // 读取文件内容
            String content = new String(file.getBytes());

            // 返回文件内容
            return ResponseEntity.ok()
                    .body("{\"code\": 200, \"message\": \"上传成功\", \"data\": {\"content\": \"" + content.replace("\"", "\\\"") + "\"}}" );
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 500, \"message\": \"上传失败: " + e.getMessage() + "\"}");
        }
    }
}

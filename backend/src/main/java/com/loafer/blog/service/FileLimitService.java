package com.loafer.blog.service;

import com.loafer.blog.model.dto.FileSizeLimitDTO;

/**
 * 文件大小限制服务
 * @author loafer
 * @since 2026-03-12 20:49:35
 **/
public interface FileLimitService {

    /**
     * 获取文件大小限制
     * @return 文件大小限制
     */
    FileSizeLimitDTO getFileLimits();

    /**
     * 更新文件大小限制
     * @param dto 文件大小限制
     * @return 文件大小限制
     */
    FileSizeLimitDTO updateFileLimits(FileSizeLimitDTO dto);
}

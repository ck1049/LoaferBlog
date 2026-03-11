package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.dto.AnnouncementDTO;
import com.loafer.blog.model.entity.Announcement;
import com.loafer.blog.model.vo.AnnouncementVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.mapper.AnnouncementMapper;
import com.loafer.blog.service.AnnouncementService;
import com.loafer.blog.utils.XssUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public ResponseVO<List<AnnouncementVO>> getAnnouncements() {
        try {
            // 按发布时间倒序排列
            QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_time");
            List<Announcement> announcements = announcementMapper.selectList(wrapper);
            List<AnnouncementVO> announcementVOs = announcements.stream()
                    .map(AnnouncementVO::new).collect(Collectors.toList());
            return ResponseVO.success(announcementVOs);
        } catch (Exception e) {
            return ResponseVO.error("获取公告列表失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<AnnouncementVO> getAnnouncement(Long id) {
        try {
            Announcement announcement = announcementMapper.selectById(id);
            if (announcement == null) {
                return ResponseVO.error("公告不存在");
            }
            AnnouncementVO announcementVO = new AnnouncementVO(announcement);
            return ResponseVO.success(announcementVO);
        } catch (Exception e) {
            return ResponseVO.error("获取公告失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<AnnouncementVO> createAnnouncement(AnnouncementDTO announcementDTO) {
        try {
            // 防XSS处理
            String title = XssUtils.filter(announcementDTO.getTitle());
            String content = XssUtils.filter(announcementDTO.getContent());
            
            Announcement announcement = new Announcement();
            announcement.setTitle(title);
            announcement.setContent(content);
            announcement.setStatus(1);
            announcement.setCreateTime(LocalDateTime.now());
            announcement.setUpdateTime(LocalDateTime.now());
            announcementMapper.insert(announcement);
            
            AnnouncementVO announcementVO = new AnnouncementVO(announcement);
            return ResponseVO.success(announcementVO);
        } catch (Exception e) {
            return ResponseVO.error("创建公告失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<AnnouncementVO> updateAnnouncement(Long id, AnnouncementDTO announcementDTO) {
        try {
            Announcement existingAnnouncement = announcementMapper.selectById(id);
            if (existingAnnouncement == null) {
                return ResponseVO.error("公告不存在");
            }

            // 防XSS处理
            String title = XssUtils.filter(announcementDTO.getTitle());
            String content = XssUtils.filter(announcementDTO.getContent());

            existingAnnouncement.setTitle(title);
            existingAnnouncement.setContent(content);
            existingAnnouncement.setUpdateTime(LocalDateTime.now());
            announcementMapper.updateById(existingAnnouncement);
            
            AnnouncementVO announcementVO = new AnnouncementVO(existingAnnouncement);
            return ResponseVO.success(announcementVO);
        } catch (Exception e) {
            return ResponseVO.error("更新公告失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<Void> deleteAnnouncement(Long id) {
        try {
            Announcement existingAnnouncement = announcementMapper.selectById(id);
            if (existingAnnouncement == null) {
                return ResponseVO.error("公告不存在");
            }

            announcementMapper.deleteById(id);
            return ResponseVO.success(null);
        } catch (Exception e) {
            return ResponseVO.error("删除公告失败: " + e.getMessage());
        }
    }
}
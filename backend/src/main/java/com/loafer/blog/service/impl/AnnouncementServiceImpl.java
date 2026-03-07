package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.entity.Announcement;
import com.loafer.blog.mapper.AnnouncementMapper;
import com.loafer.blog.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public Map<String, Object> getAnnouncements() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 按发布时间倒序排列
            QueryWrapper<Announcement> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("create_time");
            List<Announcement> announcements = announcementMapper.selectList(wrapper);
            result.put("code", 200);
            result.put("message", "获取公告列表成功");
            result.put("data", announcements);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取公告列表失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getAnnouncement(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Announcement announcement = announcementMapper.selectById(id);
            if (announcement == null) {
                result.put("code", 400);
                result.put("message", "公告不存在");
                return result;
            }
            result.put("code", 200);
            result.put("message", "获取公告成功");
            result.put("data", announcement);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取公告失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> createAnnouncement(Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        try {
            announcementMapper.insert(announcement);
            result.put("code", 200);
            result.put("message", "创建公告成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建公告失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> updateAnnouncement(Long id, Announcement announcement) {
        Map<String, Object> result = new HashMap<>();
        try {
            Announcement existingAnnouncement = announcementMapper.selectById(id);
            if (existingAnnouncement == null) {
                result.put("code", 400);
                result.put("message", "公告不存在");
                return result;
            }

            announcement.setId(id);
            announcementMapper.updateById(announcement);
            result.put("code", 200);
            result.put("message", "更新公告成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新公告失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteAnnouncement(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Announcement existingAnnouncement = announcementMapper.selectById(id);
            if (existingAnnouncement == null) {
                result.put("code", 400);
                result.put("message", "公告不存在");
                return result;
            }

            announcementMapper.deleteById(id);
            result.put("code", 200);
            result.put("message", "删除公告成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除公告失败: " + e.getMessage());
        }
        return result;
    }
}
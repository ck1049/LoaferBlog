package com.loafer.blog.service;

import com.loafer.blog.entity.Announcement;

import java.util.Map;

public interface AnnouncementService {
    Map<String, Object> getAnnouncements();
    Map<String, Object> getAnnouncement(Long id);
    Map<String, Object> createAnnouncement(Announcement announcement);
    Map<String, Object> updateAnnouncement(Long id, Announcement announcement);
    Map<String, Object> deleteAnnouncement(Long id);
}
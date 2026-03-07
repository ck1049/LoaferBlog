package com.loafer.blog.controller;

import com.loafer.blog.entity.Announcement;
import com.loafer.blog.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public Map<String, Object> getAnnouncements() {
        return announcementService.getAnnouncements();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getAnnouncement(@PathVariable Long id) {
        return announcementService.getAnnouncement(id);
    }

    @PostMapping
    public Map<String, Object> createAnnouncement(@RequestBody Announcement announcement) {
        return announcementService.createAnnouncement(announcement);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateAnnouncement(@PathVariable Long id, @RequestBody Announcement announcement) {
        return announcementService.updateAnnouncement(id, announcement);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteAnnouncement(@PathVariable Long id) {
        return announcementService.deleteAnnouncement(id);
    }
}
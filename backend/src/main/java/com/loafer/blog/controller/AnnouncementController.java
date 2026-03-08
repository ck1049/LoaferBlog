package com.loafer.blog.controller;

import com.loafer.blog.model.dto.AnnouncementDTO;
import com.loafer.blog.model.vo.AnnouncementVO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;

    @GetMapping
    public ResponseVO<List<AnnouncementVO>> getAnnouncements() {
        return announcementService.getAnnouncements();
    }

    @GetMapping("/{id}")
    public ResponseVO<AnnouncementVO> getAnnouncement(@PathVariable Long id) {
        return announcementService.getAnnouncement(id);
    }

    @PostMapping
    public ResponseVO<AnnouncementVO> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO) {
        return announcementService.createAnnouncement(announcementDTO);
    }

    @PutMapping("/{id}")
    public ResponseVO<AnnouncementVO> updateAnnouncement(@PathVariable Long id, @RequestBody AnnouncementDTO announcementDTO) {
        return announcementService.updateAnnouncement(id, announcementDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseVO<Void> deleteAnnouncement(@PathVariable Long id) {
        return announcementService.deleteAnnouncement(id);
    }
}
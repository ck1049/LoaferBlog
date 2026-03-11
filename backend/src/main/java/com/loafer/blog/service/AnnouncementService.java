package com.loafer.blog.service;

import com.loafer.blog.model.dto.AnnouncementDTO;
import com.loafer.blog.model.vo.AnnouncementVO;
import com.loafer.blog.model.vo.ResponseVO;

import java.util.List;

public interface AnnouncementService {
    ResponseVO<List<AnnouncementVO>> getAnnouncements();
    ResponseVO<AnnouncementVO> getAnnouncement(Long id);
    ResponseVO<AnnouncementVO> createAnnouncement(AnnouncementDTO announcementDTO, Long userId);
    ResponseVO<AnnouncementVO> updateAnnouncement(Long id, AnnouncementDTO announcementDTO);
    ResponseVO<Void> deleteAnnouncement(Long id);
}
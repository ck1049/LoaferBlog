package com.loafer.blog.controller;

import com.loafer.blog.entity.Message;
import com.loafer.blog.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/receiver/{receiverId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Message>> getMessagesByReceiverId(@PathVariable Long receiverId) {
        List<Message> messages = messageService.getMessagesByReceiverId(receiverId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/history/{userId1}/{userId2}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Message>> getMessageHistory(@PathVariable Long userId1, @PathVariable Long userId2) {
        List<Message> messages = messageService.getMessageHistory(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        Message createdMessage = messageService.createMessage(message);
        return new ResponseEntity<>(createdMessage, HttpStatus.CREATED);
    }

    @PostMapping("/reply")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Message> replyMessage(@RequestBody Message message) {
        Message repliedMessage = messageService.replyMessage(message);
        return new ResponseEntity<>(repliedMessage, HttpStatus.CREATED);
    }
}

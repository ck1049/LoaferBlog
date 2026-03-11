package com.loafer.blog.controller;

import com.loafer.blog.model.entity.Message;
import com.loafer.blog.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/contacts/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Map<String, Object>>> getContactList(@PathVariable Long userId) {
        List<Map<String, Object>> contacts = messageService.getContactList(userId);
        return ResponseEntity.ok(contacts);
    }

    @PutMapping("/top/{messageId}/{isTop}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> topMessage(@PathVariable Long messageId, @PathVariable Integer isTop) {
        messageService.topMessage(messageId, isTop);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{messageId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/file")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Message> sendFileMessage(@RequestBody Message message) {
        Message sentMessage = messageService.sendFileMessage(message);
        return new ResponseEntity<>(sentMessage, HttpStatus.CREATED);
    }
}

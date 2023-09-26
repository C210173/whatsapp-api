package com.sky.whatsapp.controller;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.MessageException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Message;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.SendMessageRequest;
import com.sky.whatsapp.response.ApiResponse;
import com.sky.whatsapp.service.MessageService;
import com.sky.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        req.setUserId(user.getId());
        Message message = messageService.sendMessage(req);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatMessageHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessage(chatId, user);
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable Integer messageId, @RequestHeader("Authorization") String jwt) throws UserException, MessageException {
        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);
        ApiResponse res = new ApiResponse("message deleted successfully", false);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}

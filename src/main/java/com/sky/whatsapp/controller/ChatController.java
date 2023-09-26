package com.sky.whatsapp.controller;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Chat;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.GroupChatRequest;
import com.sky.whatsapp.request.SingleChatRequest;
import com.sky.whatsapp.response.ApiResponse;
import com.sky.whatsapp.service.ChatService;
import com.sky.whatsapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final UserService userService;


    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest,
                                                  @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(reqUser, singleChatRequest.getUserId());
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroup(req, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId) throws  ChatException {
        Chat chat = chatService.findChatById(chatId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> findAllChatByUserIdHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User reqUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findAllChatByUserId(reqUser.getId());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer charId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat= chatService.addUserToGroup(userId, charId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserFromGroupHandler(@PathVariable Integer charId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat= chatService.removeFromGroup(charId, userId, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable Integer charId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        chatService.deleteChat(charId, reqUser.getId());
        ApiResponse res = new ApiResponse("chat is deleted successfully", true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/rename")
    public ResponseEntity<Chat> renameGroupHandler(@PathVariable Integer chatId, @RequestParam String groupName,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User reqUser = userService.findUserProfile(jwt);
        Chat chat = chatService.renameGroup(chatId, groupName, reqUser);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }
}

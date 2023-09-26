package com.sky.whatsapp.service;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Chat;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.GroupChatRequest;

import java.util.List;

public interface ChatService {
    Chat createChat(User reqUser, Integer userId2) throws UserException;
    Chat findChatById(Integer chatId) throws ChatException;
    List<Chat> findAllChatByUserId(Integer userId) throws UserException;
    Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;

    Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
    Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException;
    Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException;
    void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException;
}

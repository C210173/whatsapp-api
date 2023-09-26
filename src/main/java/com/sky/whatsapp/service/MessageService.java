package com.sky.whatsapp.service;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.MessageException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Message;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.request.SendMessageRequest;

import java.util.List;

public interface MessageService {
    Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
    List<Message> getChatsMessage(Integer chatId, User reqUser) throws ChatException, UserException;
    Message findMessageById(Integer messageId) throws MessageException;
    void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;

}

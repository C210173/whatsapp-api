package com.sky.whatsapp.service;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.MessageException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Chat;
import com.sky.whatsapp.model.Message;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.repository.MessageRepository;
import com.sky.whatsapp.request.SendMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImplementation implements MessageService{
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    @Override
    public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
        User user = userService.findUserById(req.getUserId());
        Chat chat = chatService.findChatById(req.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setUser(user);
        message.setContent(req.getContent());
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getChatsMessage(Integer chatId, User reqUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)){
            throw new UserException("you are not related to this chat "+ chat.getId());
        }
        List<Message> messages = messageRepository.findByChatId(chat.getId());
        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> opt = messageRepository.findById(messageId);
        if (opt.isPresent()){
            return opt.get();
        }
        throw new MessageException("message not found with id"+messageId);
    }

    @Override
    public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);
        if (!message.getUser().getId().equals(reqUser.getId())){
            throw new UserException("you can't delete another user's message "+reqUser.getFullName());
        }
        messageRepository.deleteById(messageId);
    }
}

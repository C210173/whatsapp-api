package com.sky.whatsapp.service;

import com.sky.whatsapp.exception.ChatException;
import com.sky.whatsapp.exception.UserException;
import com.sky.whatsapp.model.Chat;
import com.sky.whatsapp.model.User;
import com.sky.whatsapp.repository.ChatRepository;
import com.sky.whatsapp.request.GroupChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatServiceImplementation implements ChatService{
    private final ChatRepository chatRepository;
    private final UserService userService;

    @Override
    public Chat createChat(User reqUser, Integer userId2) throws UserException {
        User user = userService.findUserById(userId2);
        Chat isChatExist = chatRepository.findSingleChatByUserIds(user, reqUser);
        if (isChatExist!=null){
            return isChatExist;
        }
        Chat chat = new Chat();
        chat.setCreatedBy(reqUser);
        chat.getUsers().add(user);
        chat.getUsers().add(reqUser);
        chat.setIsGroup(false);
        return chatRepository.save(chat);
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepository.findById(chatId);
        if (chat.isPresent()){
            return chat.get();
        }
        throw new ChatException("chat not found with id "+ chatId);
    }

    @Override
    public List<Chat> findAllChatByUserId(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        return chatRepository.findChatByUserId(user.getId());
    }

    @Override
    public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
        Chat group = new Chat();
        group.setIsGroup(true);
        group.setChatImage(req.getChatImage());
        group.setChatName(req.getChatName());
        group.setCreatedBy(reqUser);
        group.getAdmins().add(reqUser);
        for (Integer userId: req.getUserIds()){
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }
        return chatRepository.save(group);
    }

    @Override
    public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getAdmins().contains(reqUser)) {
                User user = userService.findUserById(userId);
                chat.getUsers().add(user);
                return chatRepository.save(chat);
            }else {
                throw new UserException("You are not admin");
            }
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getUsers().contains(reqUser)){
                chat.setChatName(groupName);
                return chatRepository.save(chat);
            }
            throw new UserException("you are not member of this group");
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
        Optional<Chat> opt=chatRepository.findById(chatId);
        if (opt.isPresent()){
            Chat chat = opt.get();
            if (!chat.getAdmins().contains(reqUser) && !chat.getUsers().contains(reqUser)) {
                throw new UserException("You can't remove another user");
            }
            User user = userService.findUserById(userId);
            chat.getUsers().remove(user);
            return chatRepository.save(chat);
        }
        throw new ChatException("chat not found with id "+chatId);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) throws ChatException, UserException {
        Optional<Chat> opt = chatRepository.findById(chatId);
        if (opt.isPresent()){
            Chat chat = opt.get();
            if (chat.getCreatedBy().getId().equals(userId)) {
                chatRepository.deleteById(chat.getId());
            } else {
                throw new UserException("You can't delete this chat");
            }
        }else {
            throw new ChatException("Chat not found with id " + chatId);
        }
    }
}

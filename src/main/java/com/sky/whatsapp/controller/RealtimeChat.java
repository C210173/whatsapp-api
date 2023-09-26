package com.sky.whatsapp.controller;

import com.sky.whatsapp.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RealtimeChat {
    private final SimpMessagingTemplate simpMessagingTemplate;
    @MessageMapping("/message")
    @SendTo("/group/public")
    public Message reciveMessage(@Payload Message message){

        simpMessagingTemplate.convertAndSend("/group/" + message.getChat().getId().toString(), message);

        return message;
    }
}

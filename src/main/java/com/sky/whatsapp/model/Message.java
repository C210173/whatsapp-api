package com.sky.whatsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(length = 1000)
    private String content;
    private LocalDateTime timestamp;
    @ManyToOne
    private User user;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;
}

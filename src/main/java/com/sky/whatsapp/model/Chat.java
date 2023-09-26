package com.sky.whatsapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chatName;
    private String chatImage;
    @ManyToMany
    private Set<User> admins = new HashSet<>();
    private Boolean isGroup;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    @ManyToMany
    private Set<User> users = new HashSet<>();
    @OneToMany
    private List<Message> messages = new ArrayList<>();
}

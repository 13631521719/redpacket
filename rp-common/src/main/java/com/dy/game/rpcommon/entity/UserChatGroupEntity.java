package com.dy.game.rpcommon.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rp_user_chat_group", schema = "redpagket")
@Data
public class UserChatGroupEntity {
    @Id
    @Column(name = "id",columnDefinition = "")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "user_code",columnDefinition = "用户编码")
    private String userCode;
    @Basic
    @Column(name = "group_code",columnDefinition = "聊天群编码")
    private String groupCode;


}

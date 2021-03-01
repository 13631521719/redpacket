package com.dy.game.rpcommon.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rp_chat_group", schema = "redpagket")
@Data
public class ChatGroupEntity {
    @Id
    @Column(name = "id",columnDefinition = "主键")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Basic
    @Column(name = "group_name",columnDefinition = "群名称")
    private String groupName;
    @Basic
    @Column(name = "group_code",columnDefinition = "群编码")
    private String groupCode;
    @Basic
    @Column(name = "person_count",columnDefinition = "群总人数")
    private Integer personCount;
    @Basic
    @Column(name = "notice",columnDefinition = "群公告")
    private String notice;
    @Basic
    @Column(name = "reamrk",columnDefinition = "群备注")
    private String reamrk;


}

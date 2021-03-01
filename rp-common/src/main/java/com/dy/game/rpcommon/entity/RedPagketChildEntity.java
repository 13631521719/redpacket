package com.dy.game.rpcommon.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rp_red_pagket_child", schema = "redpagket")
@Data
public class RedPagketChildEntity {
    @Id
    @Column(name = "id",columnDefinition = "主键")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "red_pagket_code",columnDefinition = "红包编码，逻辑外键")
    private String redPagketCode;
    @Basic
    @Column(name = "user_code",columnDefinition = "用户编码，逻辑外键")
    private String userCode;
    @Basic
    @Column(name = "money",columnDefinition = "红包金额")
    private Double money;
    @Basic
    @Column(name = "status",columnDefinition = "红包状态，已领取：1，未领取：0")
    private Integer status;
    @Basic
    @Column(name = "got_time",columnDefinition = "领取时间")
    private Timestamp gotTime;

}

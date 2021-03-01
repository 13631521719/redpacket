package com.dy.game.rpcommon.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "rp_red_pagket", schema = "redpagket")
@Data
public class RedPagketEntity {
    @Id
    @Column(name = "id",columnDefinition = "主键")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "red_pagket_code",columnDefinition = "红包编码")
    private String redPagketCode;
    @Basic
    @Column(name = "group_code",columnDefinition = "群编码")
    private String groupCode;
    @Basic
    @Column(name = "user_code",columnDefinition = "用户编码")
    private String userCode;
    @Basic
    @Column(name = "money",columnDefinition = "红包金额")
    private Double money;
    @Basic
    @Column(name = "balance",columnDefinition = "红包剩余未抢余额")
    private Double balance;
    @Basic
    @Column(name = "num",columnDefinition = "发出的红包数")
    private Integer num;
    @Basic
    @Column(name = "grabed_num",columnDefinition = "已抢红包数")
    private Integer grabedNum;
    @Basic
    @Column(name = "status",columnDefinition = "红包状态，grabing：争抢中，finish：已抢完，timeout：已过期")
    private String status;
    @Basic
    @Column(name = "message",columnDefinition = "提示语")
    private String message;
    @Basic
    @Column(name = "cover_url",columnDefinition = "封面图片地址")
    private String coverUrl;
    @Basic
    @Column(name = "send_time",columnDefinition = "发送时间")
    private Date sendTime;
    @Basic
    @Column(name = "end_time",columnDefinition = "过期时间")
    private Date endTime;


}

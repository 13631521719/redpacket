package com.dy.game.rpcommon.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "rp_user", schema = "redpagket")
@Data
public class UserEntity {
    @Id
    @Column(name = "id",columnDefinition = "")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "nickname",columnDefinition = "用户昵称")
    private String nickname;
    @Basic
    @Column(name = "user_code",columnDefinition = "用户编码")
    private String userCode;
    @Basic
    @Column(name = "balance",columnDefinition = "余额")
    private Double balance;

}

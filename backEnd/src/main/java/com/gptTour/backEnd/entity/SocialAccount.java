package com.gptTour.backEnd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class SocialAccount {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(length = 100)
    private String UUID;

    @Column(length = 20)
    @Enumerated(EnumType.STRING)
    private SocialEnum provider;
}

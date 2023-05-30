package com.gptTour.backEnd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Table(name = "search_output")
@Entity
public class SearchOutput {

    @Id @GeneratedValue
    @Column(name = "output_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;
    
    // 내용 출력하는 칼럼 추가해야함
}

package com.mwork.main.entity.post;

import com.mwork.main.entity.member.Member;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode
public class Board {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    @NonNull
    private String title;

    @Column(length = 2000)
    private String article;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int count;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "board")
    private List<Comment> commentList = new ArrayList<>();

    private LocalDateTime createdDate;


    private LocalDateTime modifiedDate;

    @PrePersist
    public void preCreatedDate() {
        createdDate = LocalDateTime.now();
    }
}

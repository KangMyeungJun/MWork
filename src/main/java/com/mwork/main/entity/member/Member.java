package com.mwork.main.entity.member;

import com.mwork.main.entity.post.Board;
import com.mwork.main.entity.post.Comment;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String oAuth2Id;

    @Column(unique = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    @Column
    private LocalDateTime createdDate;


    @OneToMany(mappedBy = "member")
    private List<Board> boardList = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> commentList = new ArrayList<>();

    @PrePersist
    public void preCreatedDate() {
        createdDate = LocalDateTime.now();
    }


}

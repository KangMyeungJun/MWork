package com.mwork.main.entity.post;

import com.mwork.main.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@Table(name = "comments")
public class Comment {

    @Id @GeneratedValue
    private long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Setter(AccessLevel.NONE)
    @OneToMany(fetch = LAZY,mappedBy = "parentComment",cascade = CascadeType.ALL)
    private List<Comment> childComments = new ArrayList<>();




}

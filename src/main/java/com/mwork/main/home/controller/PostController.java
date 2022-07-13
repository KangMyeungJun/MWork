package com.mwork.main.home.controller;

import com.mwork.main.auth.Oauth2Service;
import com.mwork.main.auth.TokenService;
import com.mwork.main.entity.member.Member;
import com.mwork.main.entity.post.*;
import com.mwork.main.home.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final Oauth2Service oauth2Service;
    private final TokenService tokenService;

    @GetMapping("/")
    public String addPost() {
        return "post";
    }


    @PostMapping (value = "/")
    public String postForm(BoardForm boardForm,@AuthenticationPrincipal Member member) {
        Board board = new Board();
        board.setArticle(boardForm.getResult());
        board.setTitle(boardForm.getTitle());
        board.setMember(member);

        Board savedBoard = postService.saveBoard(board);
        log.info("savedBoard = {}",savedBoard);
        return "redirect:/post/";
    }

    @GetMapping("/pages")
    public String postPages(@PageableDefault(size = 9) Pageable pageable,
                            Model model,
                            @RequestParam(value = "keyword",defaultValue = "") String keyword,
                            @RequestParam(defaultValue = "5") Integer size,
                            @RequestParam(required = false) String sort,
                            @RequestParam(required = false) String page) {


        Page<Board> all = postService.findAllByTitle(keyword,pageable,sort);

        int boardCount = postService.findBoardCount(keyword);
        int pageNumber = all.getNumber() + 1;
        int pageSize = pageable.getPageSize();

        model.addAttribute("nextFlag",true);
        if (pageSize * pageNumber <= boardCount) {
            model.addAttribute("nextFlag",false);
        }
       
        model.addAttribute("posts",all);
        model.addAttribute("size",size);
        model.addAttribute("page",page);
        model.addAttribute("sort",sort);
        model.addAttribute("keyword",keyword);


        return "pages";
    }

    @GetMapping("/{id}")
    public String pageDetail(@PathVariable Long id,Model model,
                             @AuthenticationPrincipal Member member) {

        postService.addCount(id);
        Board findGetPost = getBoard(id);

        List<Comment> comments = postService.searchCommentByBoardId(id);
        model.addAttribute("comments",comments);

        boolean udCondition = addUdcondition(member, findGetPost);

        model.addAttribute("udCondition",udCondition);
        model.addAttribute("item",findGetPost);

        return "detail";
    }

    private boolean addUdcondition( Member member, Board findGetPost) {
        if (member != null && findGetPost.getMember().getId().equals(member.getId())) {
            return true;
        }
        return false;
    }


    @GetMapping("/edit/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Board findGetPost = getBoard(id);
        model.addAttribute("item", findGetPost);

        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmitPost(@PathVariable Long id,BoardForm boardForm) {
        Board board = getBoard(id);

        board.setTitle(boardForm.getTitle());
        board.setArticle(boardForm.getResult());
        board.setModifiedDate(LocalDateTime.now());

        postService.saveBoard(board);
        return "redirect:/post/{id}";
    }

    @PostMapping("/delete/{id}")
    public String delBoard(@PathVariable Long id) {
        long delCount = postService.deleteBoard(id);
        log.info("delCount = {}",delCount);

        return "redirect:/post/";
    }

    @PostMapping("/{id}/comment")
    public String addComment(@PathVariable Long id, CommentForm commentForm,
                             @AuthenticationPrincipal Member member) {

        Board board = getBoard(id);

        Comment comment = new Comment();
        comment.setBoard(board);
        comment.setContent(commentForm.getContent());
        comment.setMember(member);
        postService.saveComment(comment);

        return "redirect:/post/{id}";
    }

    @PostMapping("/{boardId}/comment/{commentId}")
    public String addComments(@PathVariable Long boardId,
                               @PathVariable(required = false) Long commentId,
                               CommentForm commentForm,
                              @AuthenticationPrincipal Member member) {

        Board board = getBoard(boardId);

        Comment comment = new Comment();
        if (commentId != null) {
            Comment parentComemnt = getComment(commentId);
            comment.setParentComment(parentComemnt);
        }

        comment.setBoard(board);
        comment.setContent(commentForm.getContent());
        comment.setMember(member);
        postService.saveComment(comment);

        return "redirect:/post/{boardId}";
    }



    @PostMapping("/{boardId}/comment/{commentId}/delete")
    public String delComment(@PathVariable Long boardId,
                             @PathVariable Long commentId,
                             @AuthenticationPrincipal Member member) {

        Comment findComment = getComment(commentId);

        String containsOfComments = isContainsOfComments(member, findComment);
        if (containsOfComments != null) {
            return containsOfComments;
        }

        findComment.setContent("삭제된 댓글입니다.  (" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + ")");
        findComment.setDelFlag(DelFlag.DELETE);
        findComment.setModifiedDate(LocalDateTime.now());

        postService.saveComment(findComment);

        return "redirect:/post/{boardId}";
    }


    @PostMapping("/{boardId}/comment/{commentId}/update")
    public String updateComment(@PathVariable Long boardId,
                                @PathVariable Long commentId,
                                @AuthenticationPrincipal Member member,
                                CommentForm commentForm) {
        Comment findComment = getComment(commentId);

        String containsOfComments = isContainsOfComments(member, findComment);
        if (containsOfComments != null) {
            return containsOfComments;
        }

        findComment.setContent(commentForm.getContent());
        findComment.setModifiedDate(LocalDateTime.now());

        postService.saveComment(findComment);

        return "redirect:/post/{boardId}";

    }

    private String isContainsOfComments(Member member, Comment findComment) {
        List<Comment> commentList = postService.searchCommentByMemberId(member);
        if (!commentList.contains(findComment)) {
            return "redirect:/post/{boardId}";
        }
        return null;
    }

    private Comment getComment(Long commentId) {
        Optional<Comment> findByIdComment = postService.findByIdComment(commentId);
        if (findByIdComment.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return findByIdComment.get();
    }


    private Board getBoard(Long id) {
        Optional<Board> findPost = postService.findByIdBoard(id);
        if (findPost.isEmpty()) {
            throw new EntityNotFoundException();
        }
        return findPost.get();
    }



}

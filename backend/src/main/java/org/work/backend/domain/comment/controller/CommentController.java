package org.work.backend.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    @PostMapping("/{postId}")
    public String create() {
        return "댓글 생성";
    }

    @DeleteMapping("/{commentId}")
    public String delete() {
        return "댓글 삭제";
    }
}

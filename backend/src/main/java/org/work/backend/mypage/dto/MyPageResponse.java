package org.work.backend.mypage.dto;

import lombok.Builder;
import lombok.Getter;
import org.work.backend.domain.accesslog.AccessLog;
import org.work.backend.domain.comment.Comment;
import org.work.backend.domain.post.Post;

import java.util.List;

@Builder
@Getter
public class MyPageResponse {
    private List<Post> posts;
    private List<Comment> comments;
    private List<AccessLog> accessLogs;
}
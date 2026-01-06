package org.work.backend.domain.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.work.backend.domain.admin.AdminCommentDto;
import org.work.backend.domain.admin.AdminPostDto;
import org.work.backend.domain.admin.service.AdminService;
import org.work.backend.domain.user.CustomUserDetails;
import org.work.backend.domain.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    /** 🔐 관리자 검증 */
    private void validate(CustomUserDetails userDetails) {
        adminService.validateAdmin(userDetails);
    }

    /** 게시글 전체 */
    @GetMapping("/posts")
    public List<AdminPostDto> posts(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validate(userDetails);
        return adminService.getAllPosts();
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validate(userDetails);
        adminService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    /** 댓글 전체 */
    @GetMapping("/comments")
    public List<AdminCommentDto> comments(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validate(userDetails);
        return adminService.getAllComments();
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validate(userDetails);
        adminService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/promote")
    public ResponseEntity<?> promoteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validate(userDetails);
        userService.promoteToAdmin(id);
        return ResponseEntity.ok().build();
    }
}

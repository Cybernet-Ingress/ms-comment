package com.example.ms.comment.controller;

import com.example.ms.comment.model.request.AddCommentRequest;
import com.example.ms.comment.model.response.CommentResponse;
import com.example.ms.comment.service.abstraction.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/add")
    public void addComment(@RequestBody AddCommentRequest request) {
        commentService.addComment(request);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @PostMapping("/modify/{id}")
    public void modifyComment(@PathVariable Long id, @RequestBody AddCommentRequest request) {
        commentService.modifyComment(id, request);
    }

    @GetMapping("/{productId}")
    public CommentResponse getComment(@PathVariable Long productId) {
        return commentService.getComment(productId);
    }
}
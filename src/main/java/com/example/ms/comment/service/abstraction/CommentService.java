package com.example.ms.comment.service.abstraction;

import com.example.ms.comment.model.request.AddCommentRequest;
import com.example.ms.comment.model.response.CommentResponse;


public interface CommentService {
    void addComment(AddCommentRequest commentRequest);
    void deleteComment(Long id);
    void modifyComment(Long id, AddCommentRequest commentRequest);
    CommentResponse getComment(Long id);
}
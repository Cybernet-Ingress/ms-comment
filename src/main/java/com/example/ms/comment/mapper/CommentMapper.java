package com.example.ms.comment.mapper;

import com.example.ms.comment.model.request.AddCommentRequest;
import com.example.ms.comment.model.response.CommentResponse;
import com.example.ms.comment.dao.entity.CommentEntity;

public enum CommentMapper {
    COMMENT_MAPPER;

    public CommentEntity buildCommentEntity(AddCommentRequest request) {
        return CommentEntity
                .builder()
                .comment(request.getComment())
                .build();
    }

    public CommentResponse buildCommentResponse(CommentEntity commentEntity) {
        return CommentResponse
                .builder()
                .id(commentEntity.getId())
                .comment(commentEntity.getComment())
                .build();
    }

    public CommentEntity buildUpdateCommentEntity(AddCommentRequest request, Long id) {
        return CommentEntity
                .builder()
                .id(id)
                .comment(request.getComment())
                .build();
    }

}
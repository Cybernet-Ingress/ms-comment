package com.example.ms.comment.service.concrete;

import com.example.ms.comment.annotation.LogAnnotation;
import com.example.ms.comment.dao.entity.CommentEntity;
import com.example.ms.comment.dao.repository.CommentRepository;
import com.example.ms.comment.exception.NotFoundException;
import com.example.ms.comment.model.request.AddCommentRequest;
import com.example.ms.comment.model.response.CommentResponse;
import com.example.ms.comment.service.abstraction.CommentService;
import com.example.ms.comment.util.WeirdCommentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.example.ms.comment.mapper.CommentMapper.COMMENT_MAPPER;
import static com.example.ms.comment.model.enums.CommentStatus.DELETED;
import static com.example.ms.comment.exception.ExceptionConstants.COMMENT_NOT_FOUND_EXCEPTION;

@Service
@RequiredArgsConstructor
@LogAnnotation
public class CommentServiceHandler implements CommentService {

    private final CommentRepository commentRepository;
    private final WeirdCommentUtil weirdCommentUtil;

    @Override
    public void addComment(AddCommentRequest commentRequest) {
        commentRequest.setComment(String.valueOf(weirdCommentUtil.getClass()));
        commentRepository.save(COMMENT_MAPPER.buildCommentEntity(commentRequest));
    }

    @Override
    public void deleteComment(Long id) {
        var comment = fetchIfCommentExist(id);
        comment.setStatus(DELETED);
        comment.setModifiedAt(LocalDate.now());
        commentRepository.save(comment);
    }

    @Override
    public void modifyComment(Long id, AddCommentRequest commentRequest) {
        var comment = fetchIfCommentExist(id);
        comment.setComment(commentRequest.getComment());
        commentRepository.save(COMMENT_MAPPER.buildCommentRequest(commentRequest, id));
    }

    @Override
    public CommentResponse getComment(Long productId) {
        var comment = fetchIfCommentExist(productId);
        return COMMENT_MAPPER.buildCommentResponse(comment);
    }

    private CommentEntity fetchIfCommentExist(Long id) {
        return commentRepository.findCommentById(id)
                .orElseThrow(() -> new NotFoundException(COMMENT_NOT_FOUND_EXCEPTION));
    }
}
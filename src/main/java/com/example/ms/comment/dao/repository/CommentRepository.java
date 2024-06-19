package com.example.ms.comment.dao.repository;

import com.example.ms.comment.dao.entity.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CommentRepository extends CrudRepository<CommentEntity, Long> {
    Optional<CommentEntity>findCommentById(Long id);
}
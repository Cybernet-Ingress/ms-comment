package com.example.ms.comment

import com.example.ms.comment.dao.entity.CommentEntity
import com.example.ms.comment.dao.repository.CommentRepository
import com.example.ms.comment.exception.NotFoundException
import com.example.ms.comment.model.enums.CommentStatus
import com.example.ms.comment.model.request.AddCommentRequest
import com.example.ms.comment.service.abstraction.CommentService
import com.example.ms.comment.service.concrete.CommentServiceHandler
import com.example.ms.comment.util.WeirdCommentUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import java.time.LocalDate

import static com.example.ms.comment.exception.ExceptionConstants.COMMENT_NOT_FOUND_CODE
import static com.example.ms.comment.exception.ExceptionConstants.COMMENT_NOT_FOUND_EXCEPTION
import static com.example.ms.comment.mapper.CommentMapper.COMMENT_MAPPER

class CommentServiceHandlerTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CommentService commentService
    CommentRepository commentRepository
    WeirdCommentUtil weirdCommentUtil
    CommentStatus commentStatus

    def setup() {
        commentRepository = Mock()
        weirdCommentUtil = Mock()
        commentService = new CommentServiceHandler(commentRepository, weirdCommentUtil)
    }

    def "TestAddComment"() {
        given:
        def commentRequest = random.nextObject(AddCommentRequest)

        when:
        commentService.addComment(commentRequest)

        then:
        1 * commentRepository.save(COMMENT_MAPPER.buildCommentEntity(commentRequest))
    }

    def "TestDeleteComment success"() {
        given:
        def id = random.nextLong()
        def comment = random.nextObject(CommentEntity)

        when:
        commentService.deleteComment(id)

        then:
        1 * commentRepository.findCommentById(id) >> Optional.of(comment)
        1 * commentRepository.save(comment)
        comment.status == CommentStatus.DELETED
        comment.setModifiedAt(LocalDate.now())
        notThrown(NotFoundException)
    }

    def "TestDeleteComment error"() {
        given:
        def id = random.nextLong()
        def comment = random.nextObject(CommentEntity)
        def msg = String.format(COMMENT_NOT_FOUND_CODE, COMMENT_NOT_FOUND_EXCEPTION, id)

        when:
        commentService.deleteComment(id)

        then:
        1 * commentRepository.findCommentById(id) >> Optional.empty()
        0 * commentRepository.save(comment)
        NotFoundException ex = thrown()
        ex.message == msg

    }


    def "TestModifyComment success"() {
        given:
        def id = random.nextLong()
        def commentRequest = random.nextObject(AddCommentRequest)
        def existingComment = random.nextObject(CommentEntity)
        def buildEntity = COMMENT_MAPPER.buildCommentRequest(commentRequest, id);

        when:
        commentService.modifyComment(id, commentRequest)

        then:
        1 * commentRepository.findCommentById(id) >> Optional.of(existingComment)
        1 * commentRepository.save(buildEntity)
        existingComment.comment == commentRequest.comment
        notThrown(NotFoundException)
    }

    def "TestModifyComment error"() {
        given:
        def id = random.nextLong()
        def commentRequest = random.nextObject(AddCommentRequest)
        def buildEntity = COMMENT_MAPPER.buildCommentRequest(commentRequest, id);
        def msg = String.format(COMMENT_NOT_FOUND_CODE, COMMENT_NOT_FOUND_EXCEPTION, id)

        when:
        commentService.modifyComment(id, commentRequest)

        then:
        1 * commentRepository.findCommentById(id) >> Optional.empty()
        0 * commentRepository.save(buildEntity)
        NotFoundException ex = thrown()
        ex.message == msg
    }

    def "TestGetComment success"() {
        given:
        def productId = random.nextLong()
        def existingComment = random.nextObject(CommentEntity)

        when:
        commentService.getComment(productId)

        then:
        1 * commentRepository.findCommentById(productId) >> Optional.of(existingComment)
        existingComment.id == productId
        notThrown(NotFoundException)
    }

    def "TestGetComment error"() {
        given:
        def productId = random.nextLong()
        def existingComment = random.nextObject(CommentEntity)
        def msg = String.format(COMMENT_NOT_FOUND_CODE, COMMENT_NOT_FOUND_EXCEPTION, productId)

        when:
        commentService.getComment(productId)

        then:
        1 * commentRepository.findCommentById(productId) >> Optional.empty()
        NotFoundException ex = thrown()
        ex.message == msg
    }

}
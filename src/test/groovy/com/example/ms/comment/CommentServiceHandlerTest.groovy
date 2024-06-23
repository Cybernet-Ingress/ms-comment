package com.example.ms.comment

import com.example.ms.comment.dao.entity.CommentEntity
import com.example.ms.comment.dao.repository.CommentRepository
import com.example.ms.comment.exception.NotFoundException
import com.example.ms.comment.model.request.AddCommentRequest
import com.example.ms.comment.service.abstraction.CommentService
import com.example.ms.comment.service.concrete.CommentServiceHandler
import com.example.ms.comment.util.WeirdCommentUtil
import io.github.benas.randombeans.EnhancedRandomBuilder
import io.github.benas.randombeans.api.EnhancedRandom
import spock.lang.Specification

import static com.example.ms.comment.mapper.CommentMapper.COMMENT_MAPPER

class CommentServiceHandlerTest extends Specification {
    EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandom()
    CommentService commentService
    CommentRepository commentRepository
    WeirdCommentUtil weirdCommentUtil

    def setup() {
        commentRepository = Mock()
        weirdCommentUtil = Mock()
        commentService = new CommentServiceHandler(commentRepository, weirdCommentUtil)
    }

    def "TestAddComment"() {
        given:
        def commentRequest = random.nextObject(AddCommentRequest)
        def entity = COMMENT_MAPPER.buildCommentEntity(commentRequest)

        when:
        def request = commentService.addComment(commentRequest)

        then:
        1 * commentRepository.save(COMMENT_MAPPER.buildCommentEntity(commentRequest))
        entity == request
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
        comment.status == "DELETED"
        comment.modifiedAt.toLocalDate() == LocalDate.now()
    }

    def "TestModifyComment success"() {
        given:
        def id = new Random().nextLong()
        def commentRequest = random.nextObject(AddCommentRequest)
        def existingComment = random.nextObject(CommentEntity)

        when:
        commentService.modifyComment(id, commentRequest)

        then:
        1 * commentRepository.findCommentById(id) >> Optional.of(existingComment)
        1 * commentRepository.save(existingComment) >> Optional.of(updatedComment)
        updatedComment.comment == commentRequest.comment
        existingComment.modifiedAt.toLocalDate() == LocalDate.now()
    }

    def "TestGetComment success"() {
        given:
        def id = random.nextLong()
        def entity = COMMENT_MAPPER.buildCommentResponse(comment)
        commentRepository.findCommentById(id)

        when:
        commentService.getComment(entity)

        then:
        commentRepository.findCommentById(id)
        notThrown(NotFoundException)
    }

    def "TestGetComment error"() {
        given:
        def productId = random.nextLong()
        commentRepository.findCommentById(productId) >> Optional.empty()

        when:
        commentService.getComment(productId)

        then:
        NotFoundException ex = thrown()
        ex.message == "Comment with given ID not found!"
    }
}
package com.example.ms.comment

import com.example.ms.comment.dao.entity.CommentEntity
import com.example.ms.comment.dao.repository.CommentRepository
import com.example.ms.comment.model.enums.CommentStatus
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
        1 * commentRepository.fetchIfCommentExist(id) >> Optional.of(comment)
        1 * commentRepository.save(comment)
        assert comment.status == "DELETED"
        assert comment.modifiedAt.toLocalDate() == LocalDate.now()
    }


    def "TestModifyComment success"() {
        given:
        def id = new Random().nextLong()
        def commentRequest = random.nextObject(AddCommentRequest)
        def existingComment = random.nextObject(CommentEntity)
        def updatedComment = random.nextObject(CommentEntity)

        when:
        commentService.modifyComment(id, commentRequest)

        then:
        1 * commentRepository.fetchIfCommentExist(id) >> Optional.of(existingComment)
        1 * commentRepository.save(existingComment) >> Optional.of(updatedComment)
        assert updatedComment.comment == commentRequest.comment
        assert existingComment.modifiedAt.toLocalDate() == LocalDate.now()
    }
}
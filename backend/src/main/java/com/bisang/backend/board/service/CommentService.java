package com.bisang.backend.board.service;

import com.bisang.backend.board.domain.Comment;
import com.bisang.backend.board.repository.CommentJpaRepository;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.exception.ExceptionCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;

    public Long createComment(
            Long userId,
            Long teamUserId,
            Long postId,
            Long parentId,
            String content) {
        if(parentId == null) {
            return commentJpaRepository.save(
                    Comment.builder()
                            .boardId(postId)
                            .teamUserId(teamUserId)
                            .userId(userId)
                            .parentCommentId(null)
                            .content(content)
                            .build()
            ).getId();
        }

        return commentJpaRepository.save(
                Comment.builder()
                        .boardId(postId)
                        .teamUserId(teamUserId)
                        .userId(userId)
                        .parentCommentId(parentId)
                        .content(content)
                        .build()
        ).getId();
    }

    public Long updateComment(Long userId, Long commentId, String content) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if(!isAuthor(comment, userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        comment.updateContent(content);
        return commentJpaRepository.save(comment).getId();
    }

    public void deleteComment(Long userId, Long commentId) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(()->new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if(!isAuthor(comment, userId)) throw new BoardException(ExceptionCode.NOT_AUTHOR);

        commentJpaRepository.delete(comment);
    }

    private boolean isAuthor(
            Comment comment,
            Long userId
    ) {
        if(comment.getUserId().equals(userId)) return true;
        return false;
    }

}

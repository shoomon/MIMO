package com.bisang.backend.board.service;

import com.bisang.backend.board.domain.Board;
import com.bisang.backend.board.domain.Comment;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.repository.CommentJpaRepository;
import com.bisang.backend.common.exception.BoardException;
import com.bisang.backend.common.exception.ExceptionCode;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.common.exception.UserException;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    public Long createComment(
            Long userId,
            Long teamUserId,
            Long boardId,
            Long parentId,
            String content) {

        isTeamMember(userId, teamUserId);

        boardJpaRepository.findById(boardId)
                .orElseThrow(() -> new BoardException(ExceptionCode.BOARD_NOT_FOUND));

        if(parentId == null) {

            return commentJpaRepository.save(
                    Comment.builder()
                            .boardId(boardId)
                            .teamUserId(teamUserId)
                            .userId(userId)
                            .parentCommentId(null)
                            .content(content)
                            .build()
            ).getId();
        }

        return commentJpaRepository.save(
                Comment.builder()
                        .boardId(boardId)
                        .teamUserId(teamUserId)
                        .userId(userId)
                        .parentCommentId(parentId)
                        .content(content)
                        .build()
        ).getId();
    }

    public Long updateComment(Long userId, Long commentId, String content) {
        Comment comment = commentJpaRepository.findById(commentId)
                .orElseThrow(()->new BoardException(ExceptionCode.COMMENT_NOT_FOUND));

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

    private void isTeamMember(Long userId, Long teamUserId) {
        TeamUser user = teamUserJpaRepository.findById(teamUserId)
                .orElseThrow(() -> new TeamException(ExceptionCode.NOT_FOUND_TEAM_USER));

        if(!userId.equals(user.getUserId())) {
            throw new TeamException(ExceptionCode.NOT_FOUND_TEAM_USER);
        };
    }

}

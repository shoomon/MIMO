package com.bisang.backend.board.repository;

import com.bisang.backend.board.controller.dto.CommentDto;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.bisang.backend.board.domain.QComment.comment;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

@Repository
@RequiredArgsConstructor
public class CommentQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public CommentDto getCommentInfo(Long commentId){
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(CommentDto.class,
                        comment.id.as("commentId"),
                        comment.userId,
                        teamUser.nickname.as("userNickname"),
                        user.profileUri.as("userProfileImage"),
                        comment.content,
                        comment.createdAt,
                        comment.lastModifiedAt.as("updatedAt")
                ))
                .from(comment)
                .leftJoin(user).on(comment.userId.eq(user.id))
                .leftJoin(teamUser).on(comment.teamUserId.eq(teamUser.id))
                .where(comment.id.eq(commentId))
                .fetchOne()).orElseThrow(() -> new EntityNotFoundException("댓글 정보를 찾을 수 없습니다."));
    }

    public List<CommentDto> getCommentList(Long postId){
        List<CommentDto> comments = queryFactory
                .select(Projections.constructor(CommentDto.class,
                        comment.id.as("commentId"),
                        comment.userId,
                        teamUser.nickname.as("userNickname"),
                        user.profileUri.as("userProfileImage"),
                        comment.content,
                        comment.createdAt,
                        comment.lastModifiedAt.as("updatedAt")
                ))
                .from(comment)
                .leftJoin(user).on(comment.userId.eq(user.id))
                .leftJoin(teamUser).on(comment.teamUserId.eq(teamUser.id))
                .where(comment.boardId.eq(postId))
                .fetch();

//        if(comments.isEmpty()){
//            throw new EntityNotFoundException("댓글 정보를 찾을 수 없습니다.");
//        }
        return comments;
    }

}

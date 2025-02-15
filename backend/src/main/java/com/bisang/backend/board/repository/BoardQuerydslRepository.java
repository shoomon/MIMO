package com.bisang.backend.board.repository;

import static com.bisang.backend.board.domain.QBoard.board;
import static com.bisang.backend.board.domain.QBoardDescription.boardDescription;
import static com.bisang.backend.board.domain.QBoardImage.boardImage;
import static com.bisang.backend.board.domain.QComment.comment;
import static com.bisang.backend.board.domain.QTeamBoard.teamBoard;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;
import static com.querydsl.core.group.GroupBy.groupBy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bisang.backend.board.controller.dto.BoardInfoDto;
import com.bisang.backend.board.controller.dto.BoardThumbnailDto;
import com.bisang.backend.board.controller.dto.CommentCountDto;
import com.bisang.backend.board.controller.dto.ProfileNicknameDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.domain.QBoardImage;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public BoardInfoDto getBoardInfo(Long postId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(BoardInfoDto.class,
                        board.id,
                        board.userId,
                        board.teamUserId,
                        teamBoard.boardName,
                        board.title.as("postTitle"),
                        boardDescription.description,
                        board.likes.as("likeCount"),
                        board.viewCount,
                        board.createdAt,
                        board.lastModifiedAt.as("updatedAt")
                ))
                .from(board)
                .leftJoin(teamBoard).on(board.teamBoardId.eq(teamBoard.id)) // 게시판 종류 조인
                .leftJoin(boardDescription).on(board.description.id.eq(boardDescription.id)) // 게시글 설명 조인
                .where(board.id.eq(postId))
                .fetchOne()).orElseThrow(() -> new EntityNotFoundException("게시글 정보를 찾을 수 없습니다."));
    }

    public List<Long> getBoardIdListByTeamBoardId(
            List<Long> teamBoardId,
            Long lastReadId,
            int limit
    ) {
        return queryFactory
                .select(board.id)
                .from(board)
                .where(board.teamBoardId.in(teamBoardId)
                        .and(board.id.lt(lastReadId)))
                .orderBy(board.id.desc())
                .limit(limit)
                .fetch();
    }

    public List<SimpleBoardListDto> getBoardList(Long teamBoardId, Long offset, Integer pageSize) {
        Long curOffset = offset == null ? 0 : offset;

        return queryFactory
                .select(Projections.constructor(SimpleBoardListDto.class,
                        board.id.as("postId"),
                        Expressions.nullExpression(String.class),
                        Expressions.nullExpression(String.class),
                        board.title,
                        Expressions.nullExpression(String.class),
                        board.likes,
                        board.viewCount,
                        board.createdAt,
                        board.lastModifiedAt,
                        Expressions.nullExpression(Long.class)
                ))
                .from(board)
                .where(board.teamBoardId.eq(teamBoardId))
                .orderBy(board.createdAt.desc())
                .offset(curOffset)
                .limit(pageSize)
                .fetch();
    }

    public Map<Long, Long> getCommentCount(List<Long> boardId) {
        List<CommentCountDto> commentCount =  queryFactory
                .select(Projections.constructor(CommentCountDto.class,
                        comment.boardId,
                        comment.id.count()
                        ))
                .from(comment)
                .where(comment.boardId.in(boardId))
                .groupBy(comment.boardId)
                .fetch();

        return commentCount.stream()
                .collect(Collectors.toMap(
                        CommentCountDto::getBoardId,
                        CommentCountDto::getCount,
                        (existing, replacement) -> existing
                ));
    }

    public Map<Long, String> getImageThumbnailList(List<Long> boardId) {
        QBoardImage i = QBoardImage.boardImage;
        QBoardImage i2 = new QBoardImage("i2");

        List<BoardThumbnailDto> images = queryFactory
                .select(Projections.constructor(BoardThumbnailDto.class,
                        i.boardId,
                        i.fileUri
                ))
                .from(i)
                .where(boardImage.id.eq(
                        JPAExpressions.select(i2.id.min())
                                .from(i2)
                                .where(i2.boardId.eq(i.boardId))
                        )
                        .and(i.boardId.in(boardId)))
                .fetch();

        return images.stream()
                .collect(Collectors.toMap(
                        BoardThumbnailDto::getBoardId,
                        BoardThumbnailDto::getImageUri,
                        (existing, replacement) -> existing // 중복된 boardId가 있을 경우 첫 번째 값 유지
                ));
    }

    public Map<Long, ProfileNicknameDto> getBoardUserList(List<Long> boardId) {
        List<ProfileNicknameDto> users =  queryFactory
                .select(Projections.constructor(ProfileNicknameDto.class,
                        board.id,
                        user.profileUri,
                        teamUser.nickname
                        ))
                .from(board)
                .join(user).on(user.id.eq(board.userId))
                .join(teamUser).on(teamUser.id.eq(board.teamUserId))
                .where(board.id.in(boardId))
                .fetch();

        return users.stream()
                .collect(Collectors.toMap(ProfileNicknameDto::getBoardId, dto -> dto));
    }
}

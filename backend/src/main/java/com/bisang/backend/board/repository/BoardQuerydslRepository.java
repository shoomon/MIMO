package com.bisang.backend.board.repository;

import static com.bisang.backend.board.domain.QBoard.board;
import static com.bisang.backend.board.domain.QBoardDescription.boardDescription;
import static com.bisang.backend.board.domain.QBoardImage.boardImage;
import static com.bisang.backend.board.domain.QTeamBoard.teamBoard;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.board.controller.dto.BoardInfoDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.domain.QBoardImage;
import com.bisang.backend.board.domain.QComment;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import com.bisang.backend.board.controller.dto.BoardDto;
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

    public List<SimpleBoardListDto> getBoardList(Long teamBoardId, Long offset, Integer pageSize){
        QComment commentSub = new QComment("commentSub");
        QBoardImage boardImageSub = new QBoardImage("boardThumbnailSub");
        //todo: 디폴트 썸네일 이미지 주소
        String defaultThumbnail = "";

//todo: 여기로직이 좀 많이 이상한디
        return  queryFactory
                .select(Projections.constructor(SimpleBoardListDto.class,
                                board.id,
                                user.profileUri.as("userProfileUri"),
                                teamUser.nickname.as("userNickname"),
                                board.title.as("postTitle"),
                        JPAExpressions
                                .select(boardImageSub.fileUri
                                        .coalesce(defaultThumbnail).as("imageUrl"))
                                .from(boardImageSub)
                                .where(boardImageSub.boardId.eq(board.id))
                                .limit(1),
                                board.likes.as("likeCount"),
                                board.viewCount,
                                board.createdAt,
                                board.lastModifiedAt.as("updatedAt"),
                        JPAExpressions
                                .select(commentSub.id.count())
                                .from(commentSub)
                                .where(commentSub.boardId.eq(board.id))
                        ))
                .from(board)
                .leftJoin(teamUser).on(board.teamUserId.eq(teamUser.id))
                .leftJoin(user).on(teamUser.userId.eq(user.id))
                .where(board.teamBoardId.eq(teamBoardId))
                .groupBy(board.id, user.profileUri, teamUser.nickname,
                        board.title, board.likes, board.viewCount,
                        board.createdAt, board.lastModifiedAt)
                .orderBy(board.createdAt.desc())
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }
}

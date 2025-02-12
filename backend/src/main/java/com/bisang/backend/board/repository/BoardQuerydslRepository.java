package com.bisang.backend.board.repository;

import static com.bisang.backend.board.domain.QBoard.board;
import static com.bisang.backend.board.domain.QBoardDescription.boardDescription;
import static com.bisang.backend.board.domain.QTeamBoard.teamBoard;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

import java.util.List;
import java.util.Optional;

import com.bisang.backend.board.controller.dto.BoardInfoDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
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

    public List<SimpleBoardListDto> getBoardList(Long teamBoardId, Long offset){
        QComment commentSub = new QComment("commentSub");
        return queryFactory
                .select(Projections.constructor(SimpleBoardListDto.class,
                                board.id,
                                user.profileUri.as("userProfileUri"), // 유저 테이블에서 프로필 URI 가져오기
                                teamUser.nickname.as("userNickname"), // 팀유저 테이블에서 닉네임 가져오기
                                board.title.as("postTitle"),
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
                .leftJoin(teamUser).on(board.teamUserId.eq(teamUser.id)) // 팀유저에서 닉네임 가져오기
                .leftJoin(user).on(teamUser.userId.eq(user.id)) // 유저 정보 조인하여 프로필 URI 가져오기
                .where(board.teamBoardId.eq(teamBoardId))
                .groupBy(board.id, user.profileUri, teamUser.nickname, teamBoard.boardName,
                        board.title, boardDescription.description, board.likes, board.viewCount,
                        board.createdAt, board.lastModifiedAt)
                .fetch();
    }
}

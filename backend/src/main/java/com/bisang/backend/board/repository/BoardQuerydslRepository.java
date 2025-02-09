package com.bisang.backend.board.repository;

import static com.bisang.backend.board.domain.QBoard.board;
import static com.bisang.backend.board.domain.QBoardDescription.boardDescription;
import static com.bisang.backend.board.domain.QTeamBoard.teamBoard;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND;
import static com.bisang.backend.team.domain.QTeamUser.teamUser;
import static com.bisang.backend.user.domain.QUser.user;

import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import com.bisang.backend.board.controller.dto.BoardDto;
import com.bisang.backend.common.exception.TeamException;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BoardQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public BoardDto getBoardDetail(Long postId) {
        return Optional.ofNullable(queryFactory
                .select(Projections.constructor(BoardDto.class,
                        board.id,
                        board.userId,
                        user.profileUri.as("userProfileUri"), // 유저 테이블에서 프로필 URI 가져오기
                        teamUser.nickname.as("userNickname"), // 팀유저 테이블에서 닉네임 가져오기
                        teamBoard.boardName,
                        board.title.as("postTitle"),
                        boardDescription.description,
                        board.lastModifiedAt.as("updatedAt")
                ))
                .from(board)
                .leftJoin(teamBoard).on(board.teamBoardId.eq(teamBoard.id)) // 게시판 종류 조인
                .leftJoin(boardDescription).on(board.description.id.eq(boardDescription.id)) // 게시글 설명 조인
                .leftJoin(teamUser).on(board.teamUserId.eq(teamUser.id)) // 팀유저에서 닉네임 가져오기
                .leftJoin(user).on(teamUser.userId.eq(user.id)) // 유저 정보 조인하여 프로필 URI 가져오기
                .where(board.id.eq(postId))
                .fetchOne()).orElseThrow(() -> new EntityNotFoundException("게시글 정보를 찾을 수 없습니다."));
    }
}

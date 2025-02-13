package com.bisang.backend.board.repository;

import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.bisang.backend.board.domain.QTeamBoard.teamBoard;

@Repository
@RequiredArgsConstructor
public class TeamBoardQuerydslRepository {
    private JPAQueryFactory queryFactory;

    public List<TeamBoardDto> getTeamBoardByTeamId(Long teamId) {
        //todo: team id 기준으로 게시판 조회하고
        // 조회한 teamBoardId 기준으로 게시글 10개씩 조회해서 DTO로 묶기
        List<TeamBoardDto> teamBoardList = queryFactory
                .select(Projections.constructor(TeamBoardDto.class,
                        teamBoard.id.as("teamBoardId"),
                        teamBoard.boardName.as("teamBoardName")
                        , JPAExpressions.
                                select(Projections.constructor(SimpleBoardListDto.class,
                                        //todo: (작성하다 말음)조회해서 dto에 바인딩
                                        ))
                        ))
                .from(teamBoard)
                .where()
        return null;
    }
}

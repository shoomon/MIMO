package com.bisang.backend.board.repository;

import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamBoardQuerydslRepository {
    private JPAQueryFactory queryFactory;

    public List<TeamBoardDto> getTeamBoardByTeamId(Long teamId) {
        //todo: team id 기준으로 게시판 조회하고
        // 조회한 teamBoardId 기준으로 게시글 10개씩 조회해서 DTO로 묶기
        return null;
    }
}

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
        //todo: board id 기준으로 게시글 10개씩 조회
        return null;
    }
}

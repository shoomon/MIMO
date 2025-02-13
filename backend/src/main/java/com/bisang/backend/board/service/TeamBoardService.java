package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.BoardJpaRepository;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;
import com.bisang.backend.board.repository.TeamBoardQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamBoardService {
    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final TeamBoardQuerydslRepository teamBoardQuerydslRepository;
//todo: 게시판 + 게시판 미리보기 게시글 반환
    public TeamBoardListResponse getTeamBoardList(Long teamId) {
        TeamBoardListResponse teamBoardList = new TeamBoardListResponse(
                teamBoardQuerydslRepository.getTeamBoardByTeamId(teamId)
        );
        return null;
    }

    public String createTeamBoard(Long teamId, String title) {
        TeamBoard teamBoard = teamBoardJpaRepository.save(TeamBoard.builder()
                .teamId(teamId)
                .boardName(title)
                .build()
        );
        return teamBoard.getBoardName();
    }

    public void deleteTeamBoard(Long teamBoardId) {
        boardJpaRepository.deleteAllByTeamBoardId(teamBoardId);
        teamBoardJpaRepository.deleteById(teamBoardId);
    }
}

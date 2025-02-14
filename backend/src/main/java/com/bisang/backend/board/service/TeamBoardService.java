package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamBoardService {
    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final BoardService boardService;
    private final CommentJpaRepository commentJpaRepository;
    private final BoardImageJpaRepository boardImageJpaRepository;
    private final BoardLikeRepository boardLikeRepository;

    public TeamBoardListResponse getTeamBoardList(Long teamId) {
        List<TeamBoardDto> list = new ArrayList<>();
        List<TeamBoard> teamBoardList = teamBoardJpaRepository.findAllByTeamId(teamId);

        for(TeamBoard teamBoard : teamBoardList) {
            List<SimpleBoardListDto> boardList = boardService
                    .getPostList(teamBoard.getId(), null, 10);
            TeamBoardDto teamBoardDto = new TeamBoardDto(teamBoard.getId(), teamBoard.getBoardName(), boardList);
            list.add(teamBoardDto);
        }
        return new TeamBoardListResponse(list);
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
        List<Long> boardIdList = boardJpaRepository.findTeamBoardIdByTeamBoardId(teamBoardId);

        boardJpaRepository.deleteAllByTeamBoardId(teamBoardId);

        for(Long boardId : boardIdList){
            commentJpaRepository.deleteByBoardId(boardId);
            boardImageJpaRepository.deleteByBoardId(boardId);
            boardLikeRepository.deleteByBoardId(boardId);
        }

        teamBoardJpaRepository.deleteById(teamBoardId);
    }
}

package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.team.annotation.TeamLeader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamBoardService {
    private static final Logger logger = LoggerFactory.getLogger(TeamBoardService.class);

    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final BoardJpaRepository boardJpaRepository;
    private final BoardService boardService;
    private final CommentJpaRepository commentJpaRepository;
    private final BoardImageJpaRepository boardImageJpaRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final S3Service s3Service;

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

//    @TeamLeader
    public String createTeamBoard(Long teamId, String title) {
        TeamBoard teamBoard = teamBoardJpaRepository.save(TeamBoard.builder()
                .teamId(teamId)
                .boardName(title)
                .build()
        );
        return teamBoard.getBoardName();
    }

    @Transactional
//    @TeamLeader
    public void deleteTeamBoard(Long teamBoardId) {
        List<Long> boardIdList = boardJpaRepository.findTeamBoardIdByTeamBoardId(teamBoardId);

        boardJpaRepository.deleteAllByTeamBoardId(teamBoardId);


        for(Long boardId : boardIdList){
            commentJpaRepository.deleteByBoardId(boardId);

            deleteImage(boardId);

            boardLikeRepository.deleteByBoardId(boardId);
        }

        teamBoardJpaRepository.deleteById(teamBoardId);
    }

    private void deleteImage(Long boardId) {
        List<BoardFileDto> deletedImage = new ArrayList<>();
        List<BoardFileDto> boardFiles = boardImageJpaRepository.findByBoardId(boardId);
        for(BoardFileDto image : boardFiles){
            try{
                s3Service.deleteFile(image.fileUri());
                deletedImage.add(image);
            }catch(Exception e){
                logger.error(e.getMessage());
                for(BoardFileDto deleted : deletedImage) {
                    boardImageJpaRepository.deleteById(deleted.fileId());
                }
            }
        }
        boardImageJpaRepository.deleteByBoardId(boardId);
    }
}

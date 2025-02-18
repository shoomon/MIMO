package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.BoardFileDto;
import com.bisang.backend.board.controller.dto.SimpleBoardListDto;
import com.bisang.backend.board.controller.dto.TeamBoardDto;
import com.bisang.backend.board.controller.response.TeamBoardListResponse;
import com.bisang.backend.board.domain.TeamBoard;
import com.bisang.backend.board.repository.*;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.s3.service.S3Service;
import com.bisang.backend.team.annotation.TeamLeader;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM_USER;

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
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    public TeamBoardListResponse getTeamBoardList(User user, Long teamId) {

        isValidGuest(user, teamId);

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

    @TeamLeader
    public String createTeamBoard(Long userId, Long teamId, String title) {
        TeamBoard teamBoard = teamBoardJpaRepository.save(TeamBoard.builder()
                .teamId(teamId)
                .boardName(title)
                .build()
        );
        return teamBoard.getBoardName();
    }

    @Transactional
    @TeamLeader
    public void deleteTeamBoard(Long userId, Long teamId, Long teamBoardId) {
        List<Long> boardIdList = boardJpaRepository.findTeamBoardIdByTeamBoardId(teamBoardId);

        boardJpaRepository.deleteAllByTeamBoardId(teamBoardId);


        for(Long boardId : boardIdList){
            commentJpaRepository.deleteByBoardId(boardId);

            List<BoardFileDto> boardFiles = boardImageJpaRepository.findByBoardId(boardId);
            deleteImageFromS3(boardFiles);
            boardImageJpaRepository.deleteByBoardId(boardId);

            boardLikeRepository.deleteByBoardId(boardId);
        }

        teamBoardJpaRepository.deleteById(teamBoardId);
    }

    private void deleteImageFromS3(List<BoardFileDto> boardFiles) {
        List<BoardFileDto> deletedImage = new ArrayList<>();

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
    }

    private void isValidGuest(User user, Long teamId) {
        Team team = teamJpaRepository.findTeamById(teamId)
                .orElseThrow(()->new TeamException(NOT_FOUND_TEAM));

        TeamPrivateStatus teamPrivateStatus = team.getPrivateStatus();

        if(TeamPrivateStatus.PRIVATE == teamPrivateStatus){

            if(user == null
                    || teamUserJpaRepository
                    .findByTeamIdAndUserId(team.getId(), user.getId()).isEmpty()){
                throw new TeamException(NOT_FOUND_TEAM_USER);
            }
        }
    }
}

package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.BoardThumbnailDto;
import com.bisang.backend.board.controller.response.TeamAlbumResponse;
import com.bisang.backend.board.repository.BoardQuerydslRepository;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;
import com.bisang.backend.common.exception.TeamException;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM;
import static com.bisang.backend.common.exception.ExceptionCode.NOT_FOUND_TEAM_USER;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final BoardQuerydslRepository boardQuerydslRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final TeamUserJpaRepository teamUserJpaRepository;

    public TeamAlbumResponse getAlbumImages(
            User user,
            Long teamId,
            Long lastReadImageId,
            Integer limit
    ){

        isValidGuest(user, teamId);

        //todo: 게시글이 팀 id 가지고 있는 게 낫나
        List<Long> teamBoardId = teamBoardJpaRepository.getTeamBoardIdByTeamId(teamId);
        // 게시글 id, 게시판 id
        Map<Long, Long> teamBoardAndBoardId = boardQuerydslRepository
                .getBoardIdListByTeamBoardId(teamBoardId, lastReadImageId, limit+1);

        List<Long> boardIdList = teamBoardAndBoardId.keySet().stream().toList();

        Map<Long, String> boardImage = boardQuerydslRepository
                .getImageThumbnailList(boardIdList);

        List<BoardThumbnailDto> imageList = new ArrayList<>();

        for(Long boardId : boardImage.keySet()) {
            imageList.add(new BoardThumbnailDto(
                    teamBoardAndBoardId.get(boardId), boardId, boardImage.get(boardId)
                    )
            );
        }

        Boolean hasNext = imageList.size() > limit ? true : false;

        if(hasNext){
            imageList.remove(imageList.size()-1);
        }

        TeamAlbumResponse response = new TeamAlbumResponse(imageList, hasNext);
        return response;
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

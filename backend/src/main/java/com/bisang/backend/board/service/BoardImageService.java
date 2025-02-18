package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.BoardThumbnailDto;
import com.bisang.backend.board.controller.response.TeamAlbumResponse;
import com.bisang.backend.board.repository.BoardQuerydslRepository;
import com.bisang.backend.board.repository.TeamBoardJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    private final TeamBoardJpaRepository teamBoardJpaRepository;
    private final BoardQuerydslRepository boardQuerydslRepository;

    public TeamAlbumResponse getAlbumImages(
            Long teamId,
            Long lastReadImageId,
            Integer limit
    ){
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
}

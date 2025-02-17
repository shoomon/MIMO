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
        //todo: in으로 조회할 때마다 게시글 id를 조회할 때 순서가 일관되는지?
        // 읽은 id 다음 부분부터 읽어도 되는지 order by를 해야하는지
        // 게시글 기본키가 있으니까 정렬 안해도 되나
        // 일단 정렬함
        List<Long> boardIdList = boardQuerydslRepository
                .getBoardIdListByTeamBoardId(teamBoardId, lastReadImageId, limit+1);

        Map<Long, String> boardImage = boardQuerydslRepository
                .getImageThumbnailList(boardIdList);

        List<BoardThumbnailDto> imageList = new ArrayList<>();

        for(Long boardId : boardIdList) {
            imageList.add(new BoardThumbnailDto(boardId, boardImage.get(boardId)));
        }

        Boolean hasNext = imageList.size() > limit ? true : false;

        if(hasNext){
            imageList.remove(imageList.size()-1);
        }

        TeamAlbumResponse response = new TeamAlbumResponse(imageList, hasNext);
        return response;
    }
}

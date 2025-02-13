package com.bisang.backend.board.service;

import com.bisang.backend.board.controller.dto.AlbumImageDto;
import com.bisang.backend.board.controller.response.TeamAlbumResponse;
import com.bisang.backend.board.repository.BoardImageQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardImageService {
    private final BoardImageQuerydslRepository boardImageQuerydslRepository;

    public TeamAlbumResponse getAlbumImages(Long teamId, Long lastReadImageId, Integer limit){
        List<AlbumImageDto> imageList = boardImageQuerydslRepository
                .getImagesByTeamId(teamId, lastReadImageId, limit+1);

        Boolean hasNext = imageList.size() > limit ? true : false;

        if(hasNext){
            imageList.remove(imageList.size()-1);
        }

        TeamAlbumResponse response = new TeamAlbumResponse(imageList, hasNext);
        return response;
    }
}

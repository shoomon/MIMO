package com.bisang.backend.board.repository;

import com.bisang.backend.board.controller.dto.AlbumImageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.bisang.backend.board.domain.QBoardImage.boardImage;

@Repository
@RequiredArgsConstructor
public class BoardImageQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public List<AlbumImageDto> getImagesByTeamId(Long teamId, Long lastReadImageId, Integer limit){
        Long cursor = lastReadImageId != null ? lastReadImageId : 0;

        return queryFactory
                .select(Projections.constructor(AlbumImageDto.class,
                        boardImage.id,
                        boardImage.boardId,
                        boardImage.fileUri
                        ))
                .from(boardImage)
                .where(boardImage.teamId.eq(teamId)
                , boardImage.id.gt(cursor))
                        .groupBy(boardImage.boardId)
                .orderBy(boardImage.id.min().desc())
                .limit(limit)
                .fetch();
    }
}

package com.bisang.backend.board.repository;

import com.bisang.backend.board.controller.dto.AlbumImageDto;
import com.bisang.backend.board.domain.QBoardImage;
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

    public List<AlbumImageDto> getImagesByTeamId(List<Long> teamId, Long lastReadImageId, Integer limit){
        Long cursor = lastReadImageId != null ? lastReadImageId : 0;

        QBoardImage i = boardImage;
        QBoardImage i2 = new QBoardImage("i2");

        return queryFactory
                .select(Projections.constructor(AlbumImageDto.class,
                        i.id,
                        i.boardId,
                        i.fileUri
                        ))
                .from(i)
                .where(i.teamId.in(teamId)
                        .and(i.id.lt(cursor)))
                .orderBy(i.id.desc())
                .limit(limit)
                .fetch();
    }
}

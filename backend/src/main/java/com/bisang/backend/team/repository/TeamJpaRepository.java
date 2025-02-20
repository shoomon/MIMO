package com.bisang.backend.team.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bisang.backend.team.domain.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamJpaRepository extends JpaRepository<Team, Long> {
    Optional<Team> findTeamById(Long id);

    Optional<Team> findByIdAndAccountNumber(Long id, String teamAccountNumber);

    Boolean existsByName(String name);

    @Query(value = """
        SELECT t.team_id
        FROM team t
        JOIN team_description td 
        ON t.team_description_id = td.team_description_id
        WHERE (MATCH(t.team_name) AGAINST(:keyword IN BOOLEAN MODE) 
                    OR MATCH(td.description) AGAINST(:keyword IN BOOLEAN MODE)) 
                  AND t.private_status = 'PUBLIC' 
                LIMIT :pageSize OFFSET :pageOffset
        """, nativeQuery = true)
    List<Long> searchTeamIdByKeyword(@Param("keyword") String keyword, @Param("pageSize") Long pageSize, @Param("pageOffset") Long pageOffset);

    @Query(value = """
        SELECT count(*)
        FROM team t
        JOIN team_description td 
        ON t.team_description_id = td.team_description_id
        WHERE (MATCH(t.team_name) AGAINST(:keyword IN BOOLEAN MODE) 
           OR MATCH(td.description) AGAINST(:keyword IN BOOLEAN MODE)) 
                  AND t.private_status = 'PUBLIC'
        """, nativeQuery = true)
    Long countByKeyword(@Param("keyword") String keyword);
}

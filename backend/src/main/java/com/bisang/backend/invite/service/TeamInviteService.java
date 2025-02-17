package com.bisang.backend.invite.service;

import static com.bisang.backend.common.utils.PageUtils.SHORT_PAGE_SIZE;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bisang.backend.invite.controller.dto.TeamInviteDto;
import com.bisang.backend.invite.controller.response.TeamInvitesResponse;
import com.bisang.backend.invite.repository.TeamInviteQuerydslRepository;
import com.bisang.backend.team.annotation.TeamLeader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeamInviteService {
    private final TeamInviteQuerydslRepository teamInviteQuerydslRepository;

    @TeamLeader
    @Transactional(readOnly = true)
    public TeamInvitesResponse getTeamInvites(
        Long userId, Long teamId,  Long lastTeamInviteId
    ) {
        List<TeamInviteDto> invites = teamInviteQuerydslRepository.findTeamInvites(teamId, lastTeamInviteId);
        Boolean hasNext = invites.size() > SHORT_PAGE_SIZE;
        Integer size = hasNext ? SHORT_PAGE_SIZE : invites.size();
        Long nextLastTeamInviteId = hasNext ? invites.get(size - 1).teamInviteId() : null;
        if (hasNext) {
            invites = invites.stream()
                .limit(size)
                .toList();
        }
        return new TeamInvitesResponse(size, hasNext, nextLastTeamInviteId, invites);
    }
}

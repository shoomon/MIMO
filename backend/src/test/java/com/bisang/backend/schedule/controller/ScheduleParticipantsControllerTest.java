package com.bisang.backend.schedule.controller;

import com.bisang.backend.schedule.domain.ScheduleParticipants;
import com.bisang.backend.schedule.domain.ScheduleStatus;
import com.bisang.backend.schedule.domain.TeamSchedule;
import com.bisang.backend.schedule.repository.ScheduleParticipantsJpaRepository;
import com.bisang.backend.schedule.repository.TeamScheduleJpaRepository;
import com.bisang.backend.team.domain.Area;
import com.bisang.backend.team.domain.Team;
import com.bisang.backend.team.domain.TeamCategory;
import com.bisang.backend.team.domain.TeamDescription;
import com.bisang.backend.team.domain.TeamPrivateStatus;
import com.bisang.backend.team.domain.TeamRecruitStatus;
import com.bisang.backend.team.domain.TeamUser;
import com.bisang.backend.team.repository.TeamDescriptionJpaRepository;
import com.bisang.backend.team.repository.TeamJpaRepository;
import com.bisang.backend.team.repository.TeamUserJpaRepository;
import com.bisang.backend.team.service.TeamUserService;
import com.bisang.backend.user.domain.User;
import com.bisang.backend.user.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.bisang.backend.s3.service.S3Service.CAT_IMAGE_URI;
import static com.bisang.backend.schedule.domain.ScheduleStatus.AD_HOC;
import static com.bisang.backend.team.domain.Area.SEOUL;
import static com.bisang.backend.team.domain.TeamCategory.PET;
import static com.bisang.backend.team.domain.TeamNotificationStatus.ACTIVE;
import static com.bisang.backend.team.domain.TeamPrivateStatus.PUBLIC;
import static com.bisang.backend.team.domain.TeamRecruitStatus.ACTIVE_PUBLIC;
import static java.time.temporal.ChronoUnit.MINUTES;

@SpringBootTest
public class ScheduleParticipantsControllerTest {
    @Autowired
    UserJpaRepository userJpaRepository;

    @Autowired
    TeamJpaRepository teamJpaRepository;

    @Autowired
    TeamDescriptionJpaRepository teamDescriptionJpaRepository;

    @Autowired
    TeamScheduleJpaRepository teamScheduleJpaRepository;

    @Autowired
    TeamUserJpaRepository teamUserJpaRepository;

    @Autowired
    ScheduleParticipantsJpaRepository scheduleParticipantsJpaRepository;

    @Test
    void 더미_알림용_데이터_삽입() {
        User user1 = new User("1234567890121", "userTest1", "userTest1", "userTest1", CAT_IMAGE_URI);
        User user2 = new User("1234567890122", "userTest2", "userTest2", "userTest2", CAT_IMAGE_URI);
        User user3 = new User("1234567890123", "userTest3", "userTest3", "userTest3", CAT_IMAGE_URI);
        User user4 = new User("1234567890124", "userTest4", "userTest4", "userTest4", CAT_IMAGE_URI);
        User user5 = new User("1234567890125", "userTest5", "userTest5", "userTest5", CAT_IMAGE_URI);
        User user6 = new User("1234567890126", "userTest6", "userTest6", "userTest6", CAT_IMAGE_URI);
        User user7 = new User("1234567890127", "userTest7", "userTest7", "userTest7", CAT_IMAGE_URI);

        userJpaRepository.save(user1);
        userJpaRepository.save(user2);
        userJpaRepository.save(user3);
        userJpaRepository.save(user4);
        userJpaRepository.save(user5);
        userJpaRepository.save(user6);
        userJpaRepository.save(user7);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        users.add(user5);
        users.add(user6);
        users.add(user7);

        TeamDescription teamDescription = new TeamDescription("알림스케줄러테스트더미용");
        teamDescriptionJpaRepository.save(teamDescription);

        Team team = Team.builder()
                .teamLeaderId(user1.getId())
                .teamChatroomId(0L)
                .name("알림스케줄러테스트더미용")
                .description(teamDescription)
                .accountNumber("1234567890111")
                .recruitStatus(ACTIVE_PUBLIC)
                .privateStatus(PUBLIC)
                .teamProfileUri(CAT_IMAGE_URI)
                .areaCode(SEOUL)
                .category(PET)
                .maxCapacity(80L).build();
        teamJpaRepository.save(team);

        TeamUser leader = TeamUser.createTeamLeader(user1.getId(), team.getId(), "방장", ACTIVE);
        teamUserJpaRepository.save(leader);
        List<TeamUser> teamUsers = new ArrayList<>();
        teamUsers.add(leader);

        for (int i = 1; i < 7; i++) {
            TeamUser teamMember = TeamUser.createTeamMember(users.get(i).getId(), team.getId(), "test" + i, ACTIVE);
            teamUserJpaRepository.save(teamMember);
            teamUsers.add(teamMember);
        }

        for (int i = 1; i < 7; i++) {
            TeamUser teamUser = teamUserJpaRepository.findByTeamIdAndUserId(team.getId(), users.get(i).getId()).get();
            teamUsers.add(teamUser);
        }

        List<TeamSchedule> teamSchedules = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TeamSchedule teamSchedule = TeamSchedule.builder()
                    .teamId(team.getId())
                    .teamUserId(leader.getId())
                    .title("스케줄" + i + "입니다.")
                    .description("아무말 대잔치")
                    .location("멀티캠퍼스")
                    .price(1000L)
                    .date(LocalDateTime.now().plusMinutes(1).truncatedTo(MINUTES))
                    .maxParticipants(100L)
                    .status(AD_HOC).build();
            teamScheduleJpaRepository.save(teamSchedule);
            teamSchedules.add(teamSchedule);
        }

        for (TeamSchedule teamSchedule : teamSchedules) {
            ScheduleParticipants scheduleLeader = ScheduleParticipants.creator(teamSchedule.getId(), user1.getId(), leader.getId());
            scheduleParticipantsJpaRepository.save(scheduleLeader);
            for (int i = 1; i < 7; i++) {
                ScheduleParticipants scheduleParticipants = ScheduleParticipants.participants(teamSchedule.getId(), users.get(i).getId(), teamUsers.get(i).getId());
                scheduleParticipantsJpaRepository.save(scheduleParticipants);
            }
        }
    }
}
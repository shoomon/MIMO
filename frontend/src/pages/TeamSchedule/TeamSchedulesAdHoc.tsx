import { getAdhocSchedules } from '@/apis/TeamAPI';
import CardSchedule from '@/components/molecules/CardSchedule/CardSchedule';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { TeamSchedulesResponse, TeamSimpleScheduleDto } from '@/types/Team';
import { useQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router-dom';
import BaseLayout from '../layouts/BaseLayout';
import ButtonLayout from '../layouts/ButtonLayout';
import { ButtonDefault, Title } from '@/components/atoms';
import BodyLayout_64 from '../layouts/BodyLayout_64';

const TeamSchedulesAdHoc = () => {
    const navigate = useNavigate();

    const { teamId } = useParams<{
        teamId: string;
    }>();

    const { data: sceduleData } = useQuery<TeamSchedulesResponse>({
        queryKey: ['adHocScheduleList', teamId],
        queryFn: () => {
            if (!teamId) {
                throw new Error('teamBoardId is undefined');
            }
            return getAdhocSchedules(Number(teamId));
        },
        enabled: Boolean(teamId),
    });

    const { data: myProfileData } = useMyTeamProfile(teamId);

    if (!sceduleData) {
        return <div>잘못된 응답값이다.</div>;
    }

    const adHocScheduleList = sceduleData.schedules.map(
        (schedule: TeamSimpleScheduleDto) => (
            <div className="flex flex-col gap-4">
                <CardSchedule
                    key={schedule.teamScheduleId}
                    scheduledDateTime={schedule.date}
                    label={schedule.title}
                    entryFee={schedule.price}
                    memberList={schedule.profileUris} // 변환된 memberList 사용
                    detailLink={`/team/${teamId}/schedule/${schedule.teamScheduleId}`}
                />
            </div>
        ),
    );
    return (
        <BaseLayout>
            <ButtonLayout>
                {myProfileData?.role != 'GUEST' && (
                    <ButtonDefault
                        content="글쓰기"
                        iconId="Add"
                        iconType="svg"
                        type="default"
                        onClick={() => {
                            navigate('create');
                        }}
                    />
                )}
            </ButtonLayout>
            <BodyLayout_64>
                <div className="w-full">
                    <Title label="번개모임"></Title>
                </div>
                <div className="grid w-full grid-cols-2 justify-items-start gap-y-30">
                    {adHocScheduleList.length > 0
                        ? adHocScheduleList
                        : '게시글이 없습니다.'}
                </div>
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default TeamSchedulesAdHoc;

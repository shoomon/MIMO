import { useNavigate, useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import ListContainer from '@/components/organisms/Carousel/ListContainer';
import {
    getAdhocSchedules,
    getRegularSchedules,
    getClosedSchedules,
} from '../../apis/TeamAPI';
import CardSchedule from '@/components/molecules/CardSchedule/CardSchedule';
import { TeamSimpleScheduleDto } from '@/types/Team';
import { ButtonDefault } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import ButtonLayout from '../layouts/ButtonLayout';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const TeamSchedule = () => {
    const { teamId } = useParams<{ teamId: string }>();

    const { data: myProfileData } = useMyTeamProfile(teamId);

    const navigate = useNavigate();
    const { data: regularRes, isLoading: isLoadingRegular } = useQuery({
        queryKey: ['regularSchedules', teamId],
        queryFn: () => getRegularSchedules(Number(teamId)),
        enabled: !!teamId,
    });

    const { data: adhocRes, isLoading: isLoadingAdhoc } = useQuery({
        queryKey: ['adhocSchedules', teamId],
        queryFn: () => getAdhocSchedules(Number(teamId)),
        enabled: !!teamId,
    });

    const { data: closedRes, isLoading: isLoadingClosed } = useQuery({
        queryKey: ['closedSchedules', teamId],
        queryFn: () => getClosedSchedules(Number(teamId)),
        enabled: !!teamId,
    });

    if (isLoadingRegular || isLoadingAdhoc || isLoadingClosed) {
        return <div>로딩 중...</div>;
    }

    const regularSchedules = regularRes?.schedules || [];
    const adhocSchedules = adhocRes?.schedules || [];
    const closedSchedules = closedRes?.schedules || [];

    const renderScheduleCards = (schedules: TeamSimpleScheduleDto[]) => {
        return schedules.map((schedule) => {
            const detailLink = `/team/${teamId}/schedule/${schedule.teamScheduleId}`;
            return (
                <CardSchedule
                    key={schedule.teamScheduleId}
                    scheduledDateTime={schedule.date}
                    label={schedule.title}
                    entryFee={schedule.price}
                    memberList={schedule.profileUris} // 변환된 memberList 사용
                    detailLink={detailLink}
                />
            );
        });
    };

    return (
        <BaseLayout>
            <ButtonLayout>
                {myProfileData?.role != 'GUEST' && (
                    <ButtonDefault
                        content="일정 생성"
                        iconId="PlusCalendar"
                        iconType="svg"
                        onClick={() => {
                            navigate('create');
                        }}
                    />
                )}
            </ButtonLayout>
            <BodyLayout_64>
                <>
                    <div className="w-full">
                        <ListContainer
                            label="정기 모임 🗓️"
                            items={renderScheduleCards(regularSchedules)}
                            to={`/team/${teamId}/schedule/regular`}
                            gap="4"
                            content="등록된 정기 모임이 없습니다."
                        />
                    </div>
                    <div className="w-full">
                        <ListContainer
                            label="번개 모임 ⚡"
                            items={renderScheduleCards(adhocSchedules)}
                            to={`/team/${teamId}/schedule/ad-hoc`}
                            gap="4"
                            content="등록된 번개 모임이 없습니다."
                        />
                    </div>
                    <div className="w-full">
                        <ListContainer
                            label="종료된 모임 🕒"
                            items={renderScheduleCards(closedSchedules)}
                            to={`/team/${teamId}/schedule/closed`}
                            gap="4"
                            content="종료된 모임이 없습니다."
                        />
                    </div>
                </>
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default TeamSchedule;

import { useNavigate, useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import ListContainer from '@/components/organisms/ListContainer';
import {
    getAdhocSchedules,
    getRegularSchedules,
    getClosedSchedules,
} from './../../apis/TeamAPI';
import CardSchedule from '@/components/molecules/CardSchedule/CardSchedule';
import { TeamSimpleScheduleDto } from '@/types/Team';
import { ButtonDefault } from '@/components/atoms';

const TeamSchedule = () => {
    const { teamId } = useParams<{ teamId: string }>();
    const hasPermission = true;
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

    const renderScheduleCards = (
        schedules: TeamSimpleScheduleDto[],
        isClosed: boolean = false,
    ) => {
        return schedules.map((schedule) => {
            const detailLink = `/team/${teamId}/schedule/${schedule.teamScheduleId}`; // 템플릿 리터럴 문법 수정

            return (
                <CardSchedule
                    key={schedule.teamScheduleId}
                    scheduledDateTime={schedule.date}
                    label={schedule.title}
                    entryFee={schedule.price}
                    memberList={schedule.profiles} // 변환된 memberList 사용
                    detailLink={detailLink}
                    isClosed={isClosed}
                />
            );
        });
    };

    return (
        <section className="flex flex-col gap-2">
            <div className="flex min-h-[43px] items-start justify-end self-stretch py-2">
                {hasPermission && (
                    <ButtonDefault
                        content="일정 생성"
                        iconId="PlusCalendar"
                        iconType="svg"
                        onClick={() => {
                            navigate('create');
                        }}
                    />
                )}
            </div>
            <BodyLayout_64>
                <>
                    <div className="w-full">
                        <ListContainer
                            label="정기 모임 🗓️"
                            items={renderScheduleCards(regularSchedules)}
                            to="/team/schedule/regular"
                            gap="4"
                        />
                    </div>
                    <div className="w-full">
                        <ListContainer
                            label="번개 모임 ⚡"
                            items={renderScheduleCards(adhocSchedules)}
                            to="/team/schedule"
                            gap="4"
                        />
                    </div>
                    <div className="w-full">
                        <ListContainer
                            label="종료된 모임 🕒"
                            items={renderScheduleCards(closedSchedules, true)}
                            to="/team/schedule"
                            gap="4"
                        />
                    </div>
                </>
            </BodyLayout_64>
        </section>
    );
};

export default TeamSchedule;

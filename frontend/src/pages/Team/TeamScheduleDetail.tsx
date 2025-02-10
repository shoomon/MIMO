import { ButtonDefault, Icon, Title } from '@/components/atoms';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import {
    getSpecificSchedule,
    joinSchedule,
    leaveSchedule,
} from '@/apis/TeamAPI';
import { useEffect, useState } from 'react';
import { ScheduleStatus, ScheduleStatusName } from '@/types/Team';
import { renderMemberProfiles } from './../../components/molecules/MemberProfileImageList';

const TeamScheduleDetail = () => {
    const navigate = useNavigate();
    const userId = 1; // 현재 로그인된 사용자 ID (예제)
    const { teamId, scheduleId } = useParams();
    const [isJoined, setIsJoined] = useState(false);

    // 일정 상세 정보 가져오기
    const { data: scheduleDetail, isLoading: detailLoading } = useQuery({
        queryKey: ['scheduleDetail', scheduleId],
        queryFn: () => getSpecificSchedule(Number(teamId), Number(scheduleId)),
        enabled: !!teamId && !!scheduleId,
    });

    // 유저가 일정에 참여 중인지 확인
    useEffect(() => {
        if (scheduleDetail?.profiles) {
            const userExists = scheduleDetail.profiles.some(
                (profile) => Number(profile.userId) === userId,
            );
            setIsJoined(userExists);
        }
    }, [scheduleDetail, userId]);

    // 일정 참여 (joinSchedule)
    const joinMutation = useMutation({
        mutationFn: () => joinSchedule(Number(scheduleDetail?.teamScheduleId)),
        onSuccess: () => {
            setIsJoined(true);
        },
        onError: (error) => {
            console.error('참여 신청 실패:', error);
        },
    });

    // 일정 탈퇴 (leaveSchedule)
    const leaveMutation = useMutation({
        mutationFn: () =>
            leaveSchedule(Number(scheduleDetail?.teamScheduleId), userId),
        onSuccess: () => {
            setIsJoined(false);
        },
        onError: (error) => {
            console.error('참여 취소 실패:', error);
        },
    });

    // 일정 탈퇴 (leaveSchedule)
    const deleteMutation = useMutation({
        mutationFn: () =>
            deleteSchedule(Number(scheduleDetail?.teamScheduleId), userId),
        onSuccess: () => {
            navigate(`/team/${teamId}`);
        },
        onError: (error) => {
            console.error('삭제 실패:', error);
        },
    });

    if (detailLoading) return <p>로딩 중...</p>;

    const statusText =
        ScheduleStatusName[scheduleDetail?.status as ScheduleStatus] ||
        '알 수 없음';

    return (
        <section className="flex flex-col gap-2">
            <div className="flex min-h-[43px] items-start justify-end self-stretch py-8">
                {/* 참가 신청 / 참여 취소 버튼 */}
                {isJoined ? (
                    <ButtonDefault
                        content="참여 취소"
                        type="fail"
                        onClick={() => leaveMutation.mutate()}
                    />
                ) : (
                    <ButtonDefault
                        content="참가 신청"
                        iconId="PlusCalendar"
                        iconType="svg"
                        onClick={() => joinMutation.mutate()}
                    />
                )}

                {/* 일정 수정 / 삭제 버튼 (권한이 있을 때만) */}
                {scheduleDetail?.isTeamMember && (
                    <>
                        <ButtonDefault
                            content="일정 수정"
                            iconId="PlusCalendar"
                            iconType="svg"
                            onClick={() => navigate('/edit')}
                        />
                        <ButtonDefault
                            content="글 삭제"
                            type="fail"
                            onClick={() => deleteMutation.mutate()}
                        />
                    </>
                )}
            </div>
            <BodyLayout_64>
                <>
                    <div className="text-dark flex h-fit w-full flex-col gap-2 border-b-1 border-gray-200">
                        <Title label={statusText} />
                        <h1 className="text-display-xs text-dark font-bold">
                            {scheduleDetail?.title}
                        </h1>
                    </div>
                    <div className="text-md flex flex-col gap-2 font-medium">
                        <span className="flex items-center gap-2">
                            🗺️ {scheduleDetail?.location}
                        </span>
                        <span className="flex items-center gap-2">
                            🕜 {scheduleDetail?.date}
                        </span>
                        <span className="flex items-center gap-2">
                            🪙 참가비 : {scheduleDetail?.price}
                        </span>
                        <span className="flex items-center gap-2">
                            👑 모임장 : {scheduleDetail?.nameOfLeader}
                        </span>
                        <div className="flex flex-col gap-2">
                            <span className="text-md flex flex-col gap-2 font-medium">
                                참가 멤버 : {scheduleDetail?.profiles.length}/
                                {scheduleDetail?.maxParticipants}명
                            </span>
                            {scheduleDetail?.profiles == undefined ? (
                                <span>참가 멤버가 없습니다.</span>
                            ) : (
                                <span>
                                    {renderMemberProfiles(
                                        scheduleDetail?.profiles,
                                    )}
                                </span>
                            )}
                        </div>
                        <hr className="bg-gray-200" />
                    </div>
                    <div className="flex h-fit w-full flex-col gap-4">
                        <span className="text-dark text-xl font-bold">
                            일정 소개
                        </span>
                        <span className="h-fit w-full">
                            {scheduleDetail?.description}
                        </span>
                    </div>
                    <hr className="bg-gray-200" />

                    <div className="flex flex-col gap-2">
                        <div className="flex gap-2">
                            <span className="text-dark text-xl font-bold">
                                댓글
                            </span>
                            <span>{scheduleDetail?.comments.length}</span>
                        </div>
                        <div className="flex h-fit w-full flex-col gap-4">
                            {scheduleDetail?.comments}
                        </div>
                    </div>
                </>
            </BodyLayout_64>
        </section>
    );
};

export default TeamScheduleDetail;

import { ButtonDefault, Title } from '@/components/atoms';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import {
    getSpecificSchedule,
    joinSchedule,
    leaveSchedule,
    deleteComment,
    updateComment,
    deleteSchedule,
} from '@/apis/TeamAPI';
import { useEffect, useState } from 'react';
import { ScheduleStatus, ScheduleStatusName } from '@/types/Team';
import { TeamScheduleCommentDto } from './../../types/Team';
import { Comment, CommentWrite } from '@/components/molecules';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { dateParsing } from '@/utils';
import { renderMemberProfiles } from '@/utils/memberParsing';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const TeamScheduleDetail = () => {
    const navigate = useNavigate();

    const { teamId, scheduleId } = useParams();
    const [isJoined, setIsJoined] = useState(false);
    const { data: { teamUserId } = {} } = useMyTeamProfile(teamId);

    // 일정 상세 정보 가져오기
    const { data: scheduleDetail, isLoading: detailLoading } = useQuery({
        queryKey: ['scheduleDetail', scheduleId],
        queryFn: () => getSpecificSchedule(Number(teamId), Number(scheduleId)),
        enabled: !!teamId && !!scheduleId,
    });

    // 유저가 일정에 참여 중인지 확인
    useEffect(() => {
        if (scheduleDetail?.isTeamScheduleMember) {
            setIsJoined(scheduleDetail?.isTeamScheduleMember);
        }
    }, [scheduleDetail, scheduleDetail?.isTeamScheduleMember]);

    // 댓글 상태 관리 (초기값: API에서 받아온 데이터)
    const [comments, setComments] = useState<TeamScheduleCommentDto[]>([]);

    useEffect(() => {
        if (scheduleDetail?.comments) {
            setComments(scheduleDetail.comments);
        }
    }, [scheduleDetail]);

    // 일정 참여 (joinSchedule)
    const joinMutation = useMutation({
        mutationFn: () => joinSchedule(scheduleId?.toString() || ''),
        onSuccess: () => {
            setIsJoined(true);
        },
    });

    // 일정 탈퇴 (leaveSchedule)
    const leaveMutation = useMutation({
        mutationFn: () => leaveSchedule(scheduleId!),
        onSuccess: () => {
            setIsJoined(false);
        },
    });

    // 일정 삭제
    const deleteMutation = useMutation({
        mutationFn: () => deleteSchedule(teamId!, scheduleId!),
        onSuccess: () => {
            navigate(`/team/${teamId}`);
        },
    });

    // 댓글 삭제 (UI에서 바로 제거)
    const deleteCommentMutation = useMutation({
        mutationFn: (commentId: number) => deleteComment(teamId!, commentId),
        onMutate: async (commentId) => {
            // UI에서 바로 삭제 반영
            setComments((prevComments) =>
                prevComments.filter(
                    (comment) => comment.commentSortId !== commentId,
                ),
            );
        },
        onError: (error) => {
            console.error('댓글 삭제 실패:', error);
            alert('댓글 삭제에 실패했습니다.');
        },
    });

    // 댓글 수정 (해당 댓글만 업데이트)
    const updateCommentMutation = useMutation({
        mutationFn: ({
            teamId,
            teamScheduleCommentId,
            content,
        }: {
            teamId: string;
            teamScheduleCommentId: number;
            content: string;
        }) => updateComment(teamId!, teamScheduleCommentId, content),
        onMutate: async ({ teamScheduleCommentId, content }) => {
            // UI에서 바로 수정 반영
            setComments((prevComments) =>
                prevComments.map((comment) =>
                    comment.teamScheduleCommentId === teamScheduleCommentId
                        ? { ...comment, content }
                        : comment,
                ),
            );
        },
        onError: (error) => {
            console.error('댓글 수정 실패:', error);
            alert('댓글 수정에 실패했습니다.');
        },
    });

    if (detailLoading) return <p>로딩 중...</p>;

    const statusText =
        ScheduleStatusName[scheduleDetail?.status as ScheduleStatus] ||
        '알 수 없음';

    const safeMemberList = scheduleDetail?.profileUris ?? [];

    const memberProfiles = renderMemberProfiles(safeMemberList);

    return (
        <section className="flex flex-col gap-2">
            <div className="py- flex min-h-[43px] items-end justify-end gap-2 self-stretch">
                {/* 팀 일정 작성자라면 (내 일정) 참가 신청/참여 취소 버튼은 보이지 않음 */}
                {!scheduleDetail?.isMyTeamSchedule &&
                    (isJoined ? (
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
                    ))}

                {/* 내 일정인 경우에만 수정/삭제 버튼 렌더링 */}
                {scheduleDetail?.isMyTeamSchedule && (
                    <>
                        <ButtonDefault
                            content="일정 수정"
                            iconId="PlusCalendar"
                            iconType="svg"
                            onClick={() =>
                                navigate(
                                    `/team/${teamId}/schedule/edit/${scheduleId}`,
                                )
                            }
                        />
                        <ButtonDefault
                            content="글 삭제"
                            type="fail"
                            onClick={() => deleteMutation.mutate()}
                        />
                    </>
                )}
            </div>
            <BodyLayout_24>
                <div className="text-dark flex h-fit w-full flex-col gap-4 border-b-1 border-gray-200">
                    <Title
                        label={
                            statusText === '정기모임'
                                ? '정기모임🗓️'
                                : '번개모임⚡'
                        }
                    />

                    <h1 className="text-display-xs text-dark font-bold">
                        {scheduleDetail?.title}
                    </h1>
                </div>
                <div className="text-md flex w-full flex-col gap-2 font-medium">
                    <span className="flex items-center gap-2">
                        🗺️ {scheduleDetail?.location}
                    </span>
                    <span className="flex items-center gap-2">
                        🕜
                        {scheduleDetail?.date
                            ? dateParsing(new Date(scheduleDetail.date), true)
                            : '날짜 정보 없음'}
                    </span>
                    <span className="flex items-center gap-2">
                        🪙 참가비 : {scheduleDetail?.price}
                    </span>
                    <span className="flex items-center gap-2">
                        👑 모임장 : {scheduleDetail?.nameOfLeader}
                    </span>
                    <div className="flex flex-col gap-2">
                        <span>참가 멤버</span>
                        <span className="flex gap-2">{memberProfiles}</span>
                    </div>

                    <hr className="text-gray-200" />
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
                <div className="flex w-full flex-col gap-2">
                    <div className="flex gap-2">
                        <span className="text-dark flex text-xl font-bold">
                            댓글
                        </span>
                        <span>{comments.length}</span>
                    </div>
                    <div className="xl:grid-ros-4 grid grid-rows-1 gap-2 pr-4 md:grid-rows-2 lg:grid-rows-1">
                        {comments.length > 0 ? (
                            comments.map((item) => (
                                <Comment
                                    key={item.commentSortId}
                                    commentId={item.commentSortId}
                                    teamScheduleCommentId={
                                        item.teamScheduleCommentId
                                    }
                                    content={item.content}
                                    isReply={item.hasParent}
                                    writedate={item.time}
                                    profileImage={{
                                        nickname: item.name,
                                        profileUri: item.profileUri,
                                    }}
                                    name={item.name}
                                    onDelete={() =>
                                        deleteCommentMutation.mutate(
                                            item.commentSortId,
                                        )
                                    }
                                    onUpdate={(
                                        teamScheduleCommentId,
                                        content,
                                    ) =>
                                        updateCommentMutation.mutate({
                                            teamId: teamId!, // 부모에서 관리하는 팀 ID 사용
                                            teamScheduleCommentId, // Comment 컴포넌트에서 전달받은 ID
                                            content, // 수정된 댓글 내용
                                        })
                                    }
                                />
                            ))
                        ) : (
                            <span>댓글이 없습니다.</span>
                        )}
                    </div>
                </div>
                <CommentWrite
                    teamId={teamId}
                    teamScheduleId={scheduleId}
                    teamUserId={teamUserId}
                />
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleDetail;

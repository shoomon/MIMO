import { useEffect, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { ButtonDefault, Title } from '@/components/atoms';
import { Comment, CommentWrite } from '@/components/molecules';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { dateParsing } from '@/utils';
import { renderMemberProfiles } from '@/utils/memberParsing';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import {
    getSpecificSchedule,
    joinSchedule,
    leaveSchedule,
    deleteComment,
    updateComment,
    deleteSchedule,
} from '@/apis/TeamAPI';
import { ScheduleStatus, ScheduleStatusName } from '@/types/Team';
import { TeamScheduleCommentDto } from './../../types/Team';

const TeamScheduleDetail = () => {
    const navigate = useNavigate();
    const { teamId, scheduleId } = useParams();
    const [isJoined, setIsJoined] = useState(false);
    const { data: profileData } = useMyTeamProfile(teamId);
    const queryClient = useQueryClient();

    // 답글 작성 대상 댓글 ID (어떤 댓글에 대한 답글을 작성할지 결정)
    const [replyTarget, setReplyTarget] = useState<number | null>(null);

    // 일정 상세 정보 가져오기
    const { data: scheduleDetail, isLoading: detailLoading } = useQuery({
        queryKey: ['scheduleDetail', scheduleId],
        queryFn: () => getSpecificSchedule(Number(teamId), Number(scheduleId)),
        enabled: !!teamId && !!scheduleId,
    });

    useEffect(() => {
        if (scheduleDetail?.isTeamScheduleMember) {
            setIsJoined(scheduleDetail.isTeamScheduleMember);
        }
    }, [scheduleDetail]);

    // 댓글 상태 관리 (API에서 받아온 데이터를 저장)
    const [comments, setComments] = useState<TeamScheduleCommentDto[]>([]);
    useEffect(() => {
        if (scheduleDetail?.comments) {
            setComments(scheduleDetail.comments);
        }
    }, [scheduleDetail]);

    // 댓글 배열을 commentSortId 순으로 정렬
    const sortedComments = [...comments].sort(
        (a, b) => a.commentSortId - b.commentSortId,
    );

    // 일정 참여, 탈퇴, 삭제 Mutation (기존 코드)
    const joinMutation = useMutation({
        mutationFn: () => joinSchedule(scheduleId?.toString() || ''),
        onSuccess: () => setIsJoined(true),
    });
    const leaveMutation = useMutation({
        mutationFn: () => leaveSchedule(scheduleId!),
        onSuccess: () => setIsJoined(false),
    });
    const deleteMutation = useMutation({
        mutationFn: () => deleteSchedule(teamId!, scheduleId!),
        onSuccess: () => navigate(`/team/${teamId}`),
    });

    // 댓글 삭제 Mutation
    const deleteCommentMutation = useMutation({
        mutationFn: (commentId: number) => deleteComment(teamId!, commentId),
        onMutate: async (commentId) => {
            // UI에서 바로 삭제 반영
            setComments((prev) =>
                prev.filter((comment) => comment.commentSortId !== commentId),
            );
        },
        onError: (error) => {
            console.error('댓글 삭제 실패:', error);
            alert('댓글 삭제에 실패했습니다.');
        },
    });

    // 댓글 수정 Mutation
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
            setComments((prev) =>
                prev.map((comment) =>
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
                {profileData?.role != 'GUEST' &&
                    !scheduleDetail?.isMyTeamSchedule &&
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
                {scheduleDetail?.isMyTeamSchedule ||
                    (profileData?.role == 'LEADER' && (
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
                    ))}
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
                <hr className="border-t border-gray-700" />
                {/* 댓글 영역 */}
                <div className="flex w-full flex-col gap-2 pr-1">
                    <div className="flex items-center gap-2">
                        <span className="text-dark text-xl font-bold">
                            댓글
                        </span>
                        <span className="text-dark text-xl font-bold">
                            {comments.length}
                        </span>
                    </div>

                    <div className="gap-2">
                        {sortedComments.length > 0 ? (
                            sortedComments.map((item) => (
                                <div
                                    key={item.commentSortId}
                                    className="flex flex-col gap-2 pt-3 pb-2"
                                >
                                    {/* 댓글 렌더링: isReply가 true면 들여쓰기 적용 */}
                                    <Comment
                                        commentId={item.commentSortId}
                                        someCommentId={
                                            item.teamScheduleCommentId
                                        }
                                        content={item.content}
                                        isReply={item.hasParent} // hasParent가 true이면 대댓글(답글)
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
                                                teamId: teamId!,
                                                teamScheduleCommentId,
                                                content,
                                            })
                                        }
                                        onReply={(parentId) =>
                                            setReplyTarget(parentId)
                                        }
                                    />
                                    {/* 답글 작성 폼: replyTarget가 현재 댓글의 commentSortId와 일치하면 표시 */}
                                    {replyTarget === item.commentSortId &&
                                        !item.hasParent && (
                                            <div className="ml-8">
                                                <CommentWrite
                                                    teamId={teamId!}
                                                    teamScheduleId={scheduleId}
                                                    teamUserId={Number(
                                                        profileData?.teamUserId,
                                                    )}
                                                    parentCommentId={
                                                        item.commentSortId
                                                    }
                                                    onCommentCreated={() => {
                                                        queryClient.invalidateQueries(
                                                            {
                                                                queryKey: [
                                                                    'scheduleDetail',
                                                                    scheduleId,
                                                                ],
                                                            },
                                                        );
                                                        setReplyTarget(null);
                                                    }}
                                                />
                                            </div>
                                        )}
                                </div>
                            ))
                        ) : (
                            <span>댓글이 없습니다.</span>
                        )}
                    </div>
                </div>
                {/* 최상위 댓글 작성 폼 */}
                {profileData?.role != 'GUEST' && (
                    <CommentWrite
                        teamId={teamId!}
                        teamScheduleId={scheduleId}
                        teamUserId={Number(profileData?.teamUserId)}
                        onCommentCreated={() =>
                            queryClient.invalidateQueries({
                                queryKey: ['scheduleDetail', scheduleId],
                            })
                        }
                    />
                )}
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleDetail;

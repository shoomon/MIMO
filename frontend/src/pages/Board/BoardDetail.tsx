import { useEffect, useState } from 'react';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { ButtonDefault, Title } from '@/components/atoms';
import { Comment, CommentWrite } from '@/components/molecules';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import ImageCarousel from '@/components/organisms/Carousel/ImageCarousel';
import { dateParsing } from '@/utils';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import {
    getBoardDetail,
    deletePost,
    DeleteBoardComment,
    updateBoardComment,
} from '@/apis/TeamBoardAPI';
import { BoardDetailResponse, CommentThread } from '@/types/TeamBoard';
import BaseLayout from '../layouts/BaseLayout';

const BoardDetail = () => {
    const { postId, teamId } = useParams<{
        postId: string;
        teamId: string;
        teamBoardId: string;
    }>();

    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { data: { teamUserId } = {} } = useMyTeamProfile(teamId);

    // 댓글 작성 대상 (답글을 작성할 부모 댓글 ID)
    const [replyTarget, setReplyTarget] = useState<number | null>(null);

    // 게시글 상세 정보 가져오기
    const { data: postDetail, isLoading } = useQuery<BoardDetailResponse>({
        queryKey: ['boardDetail', postId],
        queryFn: () => {
            if (!postId) throw new Error('postId is undefined');
            return getBoardDetail(postId);
        },
        enabled: Boolean(postId),
    });

    // API에서 받아온 댓글들을 로컬 state로 관리
    const [comments, setComments] = useState<CommentThread[]>(
        postDetail?.comments || [],
    );

    useEffect(() => {
        if (postDetail?.comments) {
            setComments(postDetail.comments);
        }
    }, [postDetail?.comments]);

    // 게시글 삭제 mutation
    const deleteBoardMutation = useMutation({
        mutationFn: () => deletePost(postId!),
        onSuccess: () => {
            queryClient.cancelQueries({ queryKey: ['boardDetail', postId] });
            queryClient.removeQueries({ queryKey: ['boardDetail', postId] });
            queryClient.invalidateQueries({ queryKey: ['boardList'] });
            navigate(`../`);
        },
        onError: (error) => {
            console.error('게시글 삭제 실패', error);
            alert('게시글 삭제에 실패했습니다.');
        },
    });

    // 댓글 삭제 mutation (옵티미스틱 업데이트 생략)
    const deleteCommentMutation = useMutation({
        mutationFn: (commentId: number) =>
            DeleteBoardComment(commentId.toString()),
        onMutate: async (commentId) => {
            setComments(
                (prevThreads) =>
                    prevThreads
                        .map((thread) => {
                            if (thread.rootComment.commentId === commentId) {
                                return null;
                            }
                            const updatedChildComments = thread.comments.filter(
                                (child) => child.commentId !== commentId,
                            );
                            return {
                                ...thread,
                                comments: updatedChildComments,
                            };
                        })
                        .filter((thread) => thread !== null) as CommentThread[],
            );
        },
        onError: (error) => {
            console.error('댓글 삭제 실패:', error);
            alert('댓글 삭제에 실패했습니다.');
        },
    });

    // 댓글 수정 mutation
    const updateCommentMutation = useMutation({
        mutationFn: ({
            teamId,
            teamScheduleCommentId,
            content,
        }: {
            teamId: string;
            teamScheduleCommentId: number;
            content: string;
        }) =>
            updateBoardComment(
                teamId,
                teamScheduleCommentId.toString(),
                content,
            ),
        onMutate: async ({ teamScheduleCommentId, content }) => {
            setComments((prevComments) =>
                prevComments.map((thread) => {
                    if (
                        thread.rootComment.commentId === teamScheduleCommentId
                    ) {
                        return {
                            ...thread,
                            rootComment: { ...thread.rootComment, content },
                        };
                    }
                    const updatedChildComments = thread.comments.map((child) =>
                        child.commentId === teamScheduleCommentId
                            ? { ...child, content }
                            : child,
                    );
                    return { ...thread, comments: updatedChildComments };
                }),
            );
        },
        onError: (error) => {
            console.error('댓글 수정 실패:', error);
            alert('댓글 수정에 실패했습니다.');
        },
    });

    if (isLoading) return <p>로딩 중...</p>;

    const imageUrls = postDetail?.files.map((file) => file.fileUri) || [];

    return (
        <BaseLayout>
            <div className="flex justify-end gap-2">
                <ButtonDefault
                    content="글 수정"
                    iconId="PlusCalendar"
                    iconType="svg"
                    onClick={() => navigate(`../edit/${postId}`)}
                />
                <ButtonDefault
                    content="글 삭제"
                    type="fail"
                    onClick={() => deleteBoardMutation.mutate()}
                />
            </div>
            <BodyLayout_64>
                <div className="flex w-full flex-col gap-2">
                    <Title
                        label={postDetail?.board.boardName || '게시판'}
                        to={`../`}
                    />
                    <h1 className="text-display-xs text-dark font-extrabold">
                        {postDetail?.board.postTitle}
                    </h1>
                    <div className="flex items-center gap-2">
                        <img
                            src={postDetail?.board.userProfileUri}
                            alt={teamId}
                            className="h-[18px] w-[18px] rounded-sm object-cover"
                        />
                        <span>{postDetail?.board.userNickname}</span>
                        <span>
                            {postDetail?.board.createdAt
                                ? dateParsing(
                                      new Date(postDetail?.board.createdAt),
                                  )
                                : '날짜 정보 없음'}
                        </span>
                    </div>
                    <div className="py-10">{postDetail?.board.description}</div>
                    <div>
                        <ImageCarousel images={imageUrls} />
                    </div>
                </div>
                <div className="flex w-full flex-col gap-2 pr-4">
                    <div className="flex items-center gap-2">
                        <span className="text-dark flex text-xl font-bold">
                            댓글
                        </span>
                        <span>{comments.length}</span>
                    </div>
                    <hr className="border-gray-200" />

                    <div className="gap-2">
                        {comments.length > 0 ? (
                            comments.map((thread) => (
                                <div
                                    key={thread.rootComment.commentId}
                                    className="flex flex-col gap-2 pt-3 pb-2"
                                >
                                    {/* 루트 댓글 렌더링 */}
                                    <Comment
                                        commentId={thread.rootComment.commentId}
                                        someCommentId={
                                            thread.rootComment.commentId
                                        }
                                        profileImage={{
                                            userId: thread.rootComment.userId.toString(),
                                            profileUri:
                                                thread.rootComment
                                                    .userProfileImage,
                                            nickname:
                                                thread.rootComment
                                                    .userNickname || '익명',
                                        }}
                                        name={
                                            thread.rootComment.userNickname ||
                                            '익명'
                                        }
                                        writedate={thread.rootComment.createdAt}
                                        content={thread.rootComment.content}
                                        isReply={false}
                                        onDelete={() =>
                                            deleteCommentMutation.mutate(
                                                thread.rootComment.commentId,
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

                                    {/* 대댓글 작성 폼: 만약 현재 답글 대상이 이 댓글이면 표시 */}
                                    {replyTarget ===
                                        thread.rootComment.commentId && (
                                        <div className="ml-8">
                                            <CommentWrite
                                                teamId={teamId!}
                                                teamUserId={teamUserId!}
                                                postId={Number(postId)}
                                                parentId={
                                                    thread.rootComment.commentId
                                                }
                                                onCommentCreated={() => {
                                                    queryClient.invalidateQueries(
                                                        {
                                                            queryKey: [
                                                                'boardDetail',
                                                                postId,
                                                            ],
                                                        },
                                                    );
                                                    setReplyTarget(null); // 답글 폼 숨김
                                                }}
                                            />
                                        </div>
                                    )}

                                    {/* 자식 댓글(대댓글) 렌더링 */}
                                    {thread.comments.map((child) => (
                                        <Comment
                                            key={child.commentId}
                                            commentId={child.commentId}
                                            someCommentId={child.commentId}
                                            profileImage={{
                                                userId: child.userId.toString(),
                                                profileUri:
                                                    child.userProfileImage,
                                                nickname:
                                                    child.userNickname ||
                                                    '익명',
                                            }}
                                            name={child.userNickname || '익명'}
                                            writedate={child.createdAt}
                                            content={child.content}
                                            isReply={true}
                                            onDelete={() =>
                                                deleteCommentMutation.mutate(
                                                    child.commentId,
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
                                        />
                                    ))}
                                    <hr className="border-gray-200" />
                                </div>
                            ))
                        ) : (
                            <span>댓글이 없습니다.</span>
                        )}
                    </div>
                </div>
                <CommentWrite
                    teamId={teamId!}
                    teamUserId={teamUserId!}
                    postId={Number(postId)}
                    onCommentCreated={() =>
                        queryClient.invalidateQueries({
                            queryKey: ['boardDetail', postId],
                        })
                    }
                />
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default BoardDetail;

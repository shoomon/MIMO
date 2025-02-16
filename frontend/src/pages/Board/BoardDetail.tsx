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

const BoardDetail = () => {
    const { postId, teamId, teamBoardId } = useParams<{
        postId: string;
        teamId: string;
        teamBoardId: string;
    }>();
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const { data: { teamUserId } = {} } = useMyTeamProfile(teamId);

    // 게시글 상세 정보 가져오기
    const { data: postDetail, isLoading } = useQuery<BoardDetailResponse>({
        queryKey: ['boardDetail', postId],
        queryFn: () => {
            if (!postId) throw new Error('postId is undefined');
            return getBoardDetail(postId);
        },
        enabled: Boolean(postId),
    });

    // API에서 받아온 댓글들을 로컬 state로 관리 (TeamScheduleDetail과 동일한 패턴)
    const [comments, setComments] = useState(postDetail?.comments || []);

    console.log(comments);

    useEffect(() => {
        if (postDetail?.comments) {
            setComments(postDetail.comments);
        }
    }, [postDetail?.comments]);

    // 게시글 삭제 mutation
    const deleteBoardMutation = useMutation({
        mutationFn: () => deletePost(postId!),
        onSuccess: () => {
            // 상세 쿼리 취소 및 제거
            queryClient.cancelQueries({ queryKey: ['boardDetail', postId] });
            queryClient.removeQueries({ queryKey: ['boardDetail', postId] });
            // 목록 쿼리 invalidation으로 최신 목록을 반영
            queryClient.invalidateQueries({ queryKey: ['boardList'] });
            // 리스트 페이지로 이동
            navigate(`../`);
        },
        onError: (error) => {
            console.error('게시글 삭제 실패', error);
            alert('게시글 삭제에 실패했습니다.');
        },
    });

    // 댓글 삭제 mutation (UI에서 바로 삭제 반영)
    const deleteCommentMutation = useMutation({
        mutationFn: (commentId: number) =>
            DeleteBoardComment(commentId.toString()),
        onMutate: async (commentId) => {
            setComments(
                (prevThreads) =>
                    // 각 댓글 스레드에 대해 삭제 대상 댓글을 제거합니다.
                    prevThreads
                        .map((thread) => {
                            // 만약 루트 댓글이 삭제 대상이면, 스레드 전체를 제거
                            if (thread.rootComment.commentId === commentId) {
                                return null;
                            }
                            // 그렇지 않으면, 자식 댓글 배열에서 삭제 대상 댓글 제거
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

    // 댓글 수정 mutation (UI에서 바로 수정 반영)
    const updateCommentMutation = useMutation({
        mutationFn: ({
            teamId,
            teamScheduleCommentId,
            content,
        }: {
            teamId: string;
            teamScheduleCommentId: number;
            content: string;
        }) => updateBoardComment(teamScheduleCommentId.toString(), content),
        onMutate: async ({ teamScheduleCommentId, content }) => {
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

    if (isLoading) return <p>로딩 중...</p>;

    const parsedDate = dateParsing(new Date(postDetail?.board.createdAt));
    const imageUrls = postDetail?.files.map((file) => file.fileUri) || [];

    return (
        <section className="flex flex-col gap-2">
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
                <div className="flex flex-col gap-4">
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
                        <span>{parsedDate}</span>
                    </div>
                    <div>{postDetail?.board.description}</div>
                    <div>
                        <ImageCarousel images={imageUrls} />
                    </div>
                </div>
                <hr className="bg-gray-200" />
                <div className="flex flex-col gap-2">
                    <div className="flex gap-2">
                        <span className="text-dark flex text-xl font-bold">
                            댓글
                        </span>
                        <span>{comments.length}</span>
                    </div>
                    <div className="grid gap-2">
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
                                            item.rootComment.commentId,
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
                            ))
                        ) : (
                            <span>댓글이 없습니다.</span>
                        )}
                    </div>
                </div>
                <CommentWrite
                    teamId={teamId}
                    teamUserId={teamUserId}
                    postId={Number(postId)}
                />
            </BodyLayout_64>
        </section>
    );
};

export default BoardDetail;

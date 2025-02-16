import { getBoardDetail } from '@/apis/TeamBoardAPI';
import { BoardDetailResponse } from '@/types/TeamBoard';
import { useQuery } from '@tanstack/react-query';
import { useParams, Navigate, useNavigate } from 'react-router-dom';
import ButtonLayout from '../layouts/ButtonLayout';
import { ButtonDefault, Title } from '@/components/atoms';
import { CommentWrite } from '@/components/molecules';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import { dateParsing } from '@/utils';

const BoardDetail = () => {
    const { postId, teamId, teamBoardId } = useParams<{
        postId: string;
        teamId: string;
        teamBoardId: string;
    }>();
    const navigate = useNavigate();
    const { data: postDetail } = useQuery<BoardDetailResponse>({
        queryKey: ['boardList', postId],
        queryFn: () => {
            if (!postId) {
                throw new Error('teamBoardId is undefined');
            }
            return getBoardDetail(postId);
        },
        enabled: Boolean(postId),
    });

    // const parsedate = dateParsing(new Date(postDetail?.board.createdAt);
    return (
        <section className="flex flex-col gap-2">
            <ButtonLayout>
                <ButtonDefault
                    content="글 수정"
                    iconId="PlusCalendar"
                    iconType="svg"
                    onClick={() => navigate(`../edit/${postId}`)}
                />
                <ButtonDefault
                    content="글 삭제"
                    type="fail"
                    onClick={() => deleteMutation.mutate()}
                />
            </ButtonLayout>
            <BodyLayout_64>
                <div className="flex w-full flex-col gap-4">
                    <div className="flex-col gap-2">
                        <Title
                            label={postDetail?.board.boardName || '게시판'}
                            to={`../`}
                        />
                        <hr className="text-gray-200" />
                    </div>

                    <div className="flex flex-col gap-2">
                        <h1 className="text-display-xs text-dark font-extrabold">
                            {postDetail?.board.postTitle}
                        </h1>

                        <div className="w-full flex-col items-center gap-2 text-lg font-medium">
                            <span className="flex items-center">
                                <img
                                    src={postDetail?.board.userProfileUri}
                                    alt={teamId}
                                    className="h-[18px] w-[18px] rounded-sm object-cover"
                                />
                                {postDetail?.board.userNickname}
                            </span>
                            <span className="flex justify-between pr-4">
                                <div>
                                    조회수{' '}
                                    <span className="text-brand-primary-400 font-bold">
                                        {postDetail?.board.viewCount}
                                    </span>{' '}
                                    | 좋아요{' '}
                                    <span className="text-brand-primary-400 font-bold">
                                        {postDetail?.board.likeCount}
                                    </span>{' '}
                                    | {postDetail?.board.createdAt}
                                </div>
                            </span>
                        </div>
                    </div>
                </div>

                <CommentWrite
                    teamId={teamId}
                    // teamScheduleId=
                    teamUserId={22}
                />
            </BodyLayout_64>
        </section>
    );
};

export default BoardDetail;

import { getBoardList } from '@/apis/TeamBoardAPI';
import { ButtonDefault, Title } from '@/components/atoms';
import { Post, TeamBoardListResponse } from '@/types/TeamBoard';
import { useQuery } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router-dom';
import ButtonLayout from '../layouts/ButtonLayout';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import { CardBoard } from '@/components/molecules';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const BoardPosts = () => {
    const navigate = useNavigate();
    const { teamBoardId, teamId } = useParams<{
        teamBoardId: string;
        teamId: string;
    }>();

    const { data: boardData } = useQuery<TeamBoardListResponse>({
        queryKey: ['boardList', teamBoardId],
        queryFn: () => {
            if (!teamBoardId) {
                throw new Error('teamBoardId is undefined');
            }
            return getBoardList(teamBoardId);
        },
        enabled: Boolean(teamBoardId),
    });

    const { data: myProfileData } = useMyTeamProfile(teamId);

    if (!boardData) {
        return <div>잘못된 응답값이다.</div>;
    }

    const boardList = boardData.boardList.map((post: Post) => (
        <div className="flex flex-col gap-4">
            <CardBoard
                key={post.postId}
                userProfileUri={post.userProfileUri}
                userNickname={post.userNickname}
                postTitle={post.postTitle}
                imageUri={post.imageUri}
                likeCount={post.likeCount}
                viewCount={post.viewCount}
                createdAt={post.createdAt}
                updatedAt={post.updatedAt}
                commentCount={post.commentCount}
                layoutType="List"
                linkto={`post/${post.postId.toString()}`}
            />
            <hr className="border-gray-200" />
        </div>
    ));
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
                    <Title label={boardData.teamBoardName}></Title>
                </div>
                <div className="flex w-full flex-col gap-4">
                    {boardList.length > 0 ? boardList : '게시글이 없습니다.'}
                </div>
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default BoardPosts;

import { ButtonDefault } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import ButtonLayout from '../layouts/ButtonLayout';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { useParams } from 'react-router-dom';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { CreateTeamBoard, getTeamBoardList } from '@/apis/TeamBoardAPI';
import ListContainer from '@/components/organisms/Carousel/ListContainer';
import { TeamBoardAPIResponse, TeamBoardListResponse } from '@/types/TeamBoard';
import { CardBoard } from '@/components/molecules';
import BodyLayout_64 from '../layouts/BodyLayout_64';
const Board = () => {
    const { teamId } = useParams() as { teamId: string };

    const { data: myProfileData } = useMyTeamProfile(teamId);

    const { data } = useQuery<TeamBoardAPIResponse>({
        queryKey: ['boardList', teamId],
        queryFn: () => getTeamBoardList(teamId),
    });

    const queryClient = useQueryClient();

    const mutation = useMutation<boolean, Error, string, unknown>({
        mutationFn: (teamId: string) => CreateTeamBoard(teamId, '자유게시판12'),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['boardList', teamId] });
        },
    });

    console.log(data);

    // teamBoardList를 순회하면서 각 게시판의 ListContainer를 생성합니다.
    const boardList = data?.teamBoardList.map(
        (board: TeamBoardListResponse) => {
            // 각 게시판의 postList를 CardBoard 컴포넌트 배열로 변환
            const items =
                board.postList?.map((post) => (
                    <CardBoard
                        key={post.postId}
                        postId={post.postId}
                        boardId={board.teamBoardId!}
                        userProfileUri={post.userProfileUri}
                        userNickname={post.userNickname}
                        postTitle={post.postTitle}
                        imageUri={post.imageUri}
                        likeCount={post.likeCount}
                        viewCount={post.viewCount}
                        createdAt={post.createdAt}
                        updatedAt={post.updatedAt}
                        commentCount={post.commentCount}
                        layoutType="Card"
                    />
                )) || [];

            return (
                <ListContainer
                    key={board.teamBoardId}
                    label={board.teamBoardName}
                    to={`${board.teamBoardId}`} // 필요에 따라 링크 수정
                    gap="4"
                    items={items}
                />
            );
        },
    );

    return (
        <BaseLayout>
            <ButtonLayout>
                {myProfileData?.role === 'LEADER' && (
                    <ButtonDefault
                        content="게시판 생성"
                        iconId="Add"
                        iconType="svg"
                        type="default"
                        onClick={() => {
                            mutation.mutate(teamId);
                        }}
                    />
                )}
            </ButtonLayout>
            <BodyLayout_64>
                {boardList && boardList.length > 0 ? (
                    boardList
                ) : (
                    <div className="text-text flex h-36 items-center justify-center text-xl">
                        게시판이 존재하지 않습니다.
                    </div>
                )}
            </BodyLayout_64>
        </BaseLayout>
    );
};

export default Board;

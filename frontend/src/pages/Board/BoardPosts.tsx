import { deleteTeamBoard, getBoardList } from '@/apis/TeamBoardAPI';
import { ButtonDefault, Title } from '@/components/atoms';
import { Post, TeamBoardListResponse } from '@/types/TeamBoard';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useNavigate, useParams } from 'react-router-dom';
import ButtonLayout from '../layouts/ButtonLayout';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_64 from '../layouts/BodyLayout_64';
import { CardBoard } from '@/components/molecules';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';

const BoardPosts = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

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

    const deleteTeamBoardMutation = useMutation({
        mutationFn: (teamBoardId: string) => deleteTeamBoard(teamBoardId),
        onSuccess: () => {
            // 삭제 성공 시 캐시 정리 (필요하다면)
            queryClient.invalidateQueries({
                queryKey: ['teamboardList', teamId],
            });
            // 삭제 성공 후 이전 페이지 또는 원하는 페이지로 이동
            navigate('../');
        },
        onError: (error) => {
            console.error('게시판 삭제 실패:', error);
            alert('게시판 삭제에 실패했습니다.');
        },
    });

    const { data: myProfileData } = useMyTeamProfile(teamId);

    if (!boardData) {
        return <div>잘못된 응답값이다.</div>;
    }

    console.log(boardData);

    const boardList = boardData.boardList.map((post: Post) => (
        <div className="flex flex-col gap-4">
            <CardBoard
                key={post.boardId}
                userProfileUri={post.userProfileUri}
                userNickname={post.userNickname}
                postTitle={post.boardTitle}
                imageUri={post.imageUri}
                likeCount={post.likeCount}
                viewCount={post.viewCount}
                createdAt={post.createdAt}
                updatedAt={post.updatedAt}
                commentCount={post.commentCount}
                layoutType="List"
                linkto={`post/${post.boardId}`}
            />
            <hr className="border-gray-200" />
        </div>
    ));

    return (
        <BaseLayout>
            <ButtonLayout>
                {myProfileData?.role == 'LEADER' && (
                    <ButtonDefault
                        content="게시판 삭제"
                        type="fail"
                        onClick={() => {
                            deleteTeamBoardMutation.mutate(teamBoardId!);
                        }}
                    />
                )}
                {myProfileData?.role != 'GUEST' && (
                    <ButtonDefault
                        content="글쓰기"
                        iconId="Add"
                        iconType="svg"
                        type="default"
                        onClick={async () => {
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

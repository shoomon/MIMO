import { useState } from 'react';
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
import BasicInputModal from '@/components/molecules/BasicInputModal/BasicInputModal';

const Board = () => {
    const { teamId } = useParams() as { teamId: string };

    const { data: myProfileData } = useMyTeamProfile(teamId);

    const { data } = useQuery<TeamBoardAPIResponse>({
        queryKey: ['teamboardList', teamId],
        queryFn: () => getTeamBoardList(teamId),
    });

    const queryClient = useQueryClient();

    const mutation = useMutation<
        boolean,
        Error,
        { teamId: string; boardName: string },
        unknown
    >({
        mutationFn: ({ teamId, boardName }) =>
            CreateTeamBoard(teamId, boardName),
        onSuccess: () => {
            queryClient.invalidateQueries({
                queryKey: ['teamboardList', teamId],
            });
        },
    });

    // 팀게시판 리스트 렌더링
    const boardList = data?.teamBoardList.map(
        (board: TeamBoardListResponse) => {
            const items =
                board.boardList?.map((post) => (
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
                        layoutType="Card"
                        linkto={`${board.teamBoardId}/post/${post.boardId}`}
                    />
                )) || [];

            return (
                <>
                    <ListContainer
                        key={board.teamBoardId}
                        label={board.teamBoardName}
                        to={`${board.teamBoardId}`}
                        gap="4"
                        items={items}
                        content="게시글이 없습니다."
                    />
                </>
            );
        },
    );

    // 모달 상태 관리
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = () => setIsModalOpen(true);
    const closeModal = () => setIsModalOpen(false);

    return (
        <BaseLayout>
            <ButtonLayout>
                {myProfileData?.role === 'LEADER' && (
                    <ButtonDefault
                        content="게시판 생성"
                        iconId="Add"
                        iconType="svg"
                        type="default"
                        onClick={openModal}
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
            {/* 모달 렌더링 */}
            <BasicInputModal
                isOpen={isModalOpen}
                title="게시판 생성"
                subTitle="게시판 이름을 입력하세요"
                onConfirmClick={(boardName) => {
                    mutation.mutate({ teamId, boardName });
                    closeModal();
                }}
                onCancelClick={closeModal}
            />
        </BaseLayout>
    );
};

export default Board;

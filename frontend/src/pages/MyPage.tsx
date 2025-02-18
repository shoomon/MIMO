import BasicInputModal from '@/components/molecules/BasicInputModal/BasicInputModal';
import useCharge from '@/hooks/useCharge';
import { getMyAllInfoAPI } from '@/apis/AuthAPI';
import { useQuery } from '@tanstack/react-query';
import BaseLayout from './layouts/BaseLayout';
import BodyLayout_24 from './layouts/BodyLayout_24';
import { Icon, RatingStar, Title } from '@/components/atoms';
import { dateParsing } from '@/utils';
import { Link } from 'react-router-dom';

const MyPage = () => {
    const { isOpen, handleConfirm, handleCharge, handleCancel } = useCharge();

    const { data } = useQuery({
        queryKey: ['myAllData'],
        queryFn: getMyAllInfoAPI,
        staleTime: 1000 * 30,
        gcTime: 1000 * 60,
    });

    const displayedBoard = Array.isArray(data?.userBoard)
        ? data.userBoard.length > 3
            ? data.userBoard.slice(0, 3)
            : data.userBoard
        : [];

    const displayedComment = Array.isArray(data?.userComment)
        ? data.userComment.length > 3
            ? data.userComment.slice(0, 3)
            : data.userComment
        : [];

    return (
        <BaseLayout>
            <BodyLayout_24>
                <div className="relative h-[128px] w-[128px]">
                    <img
                        src={data?.profileUri}
                        alt="profile"
                        className="h-full w-full rounded-full"
                    />
                    <div className="absolute right-0 bottom-0 rounded-full bg-white p-2 shadow-md">
                        <Icon id="Pen" type="svg" />
                    </div>
                </div>
                <div className="flex flex-col items-center gap-2">
                    <label className="text-lg font-bold">
                        {data?.nickname}
                    </label>
                    <div className="font-light">{data?.email}</div>
                </div>
                <div className="flex items-center gap-1">
                    <Icon id="Settings" type="svg" />
                    환경설정
                </div>
                {data?.reviewScore !== undefined ? (
                    <RatingStar rating={data.reviewScore} />
                ) : (
                    <div>별점 정보가 없습니다.</div>
                )}

                <div>
                    <div>
                        <h1>마이페이지</h1>
                        <button
                            type="button"
                            className="bg-brand-primary-300 cursor-pointer p-2 text-white"
                            onClick={handleCharge}
                        >
                            충전하기
                        </button>
                        <BasicInputModal
                            isOpen={isOpen}
                            title="충전 금액을 입력해주세요."
                            subTitle="100원 이상부터 충전 가능합니다."
                            inputPlaceholder="금액을 입력하세요."
                            confirmLabel="충전하기"
                            onConfirmClick={handleConfirm}
                            onCancelClick={handleCancel}
                        />
                    </div>
                </div>

                {/* 🟢 내가 쓴 글 & 댓글을 동등한 크기로 유지하는 컨테이너 */}
                <div className="flex w-full gap-4">
                    {/* 🟢 내가 쓴 글 */}
                    <div className="min-w-0 flex-1">
                        <section className="flex flex-col gap-6">
                            <Title label="내가 쓴 글" to="current" />
                            <div className="flex min-h-[313px] flex-col gap-4">
                                {displayedBoard.map((item) => (
                                    <Link
                                        key={item.boardTeamInfo.boardId}
                                        to={`/team/${item.boardTeamInfo.teamId}/board/${item.boardTeamInfo.teamBoardId}/post/${item.boardTeamInfo.boardId}`}
                                        className="flex gap-4"
                                    >
                                        <img
                                            src={item.imageUri}
                                            className="h-[100px] w-[100px] rounded-xl border border-gray-200 object-cover"
                                        />
                                        <div className="flex min-w-0 flex-1 flex-col justify-between py-2">
                                            <div className="flex flex-col gap-1">
                                                {/* 🟢 글 제목이 길어지면 `...` 처리 */}
                                                <span className="truncate text-xl font-bold">
                                                    {
                                                        item.boardTeamInfo
                                                            .boardTitle
                                                    }
                                                </span>
                                                <span>
                                                    {
                                                        item.boardTeamInfo
                                                            .teamName
                                                    }
                                                </span>
                                            </div>
                                            <span>
                                                {dateParsing(
                                                    new Date(
                                                        item.boardTeamInfo.boardCreatedAt,
                                                    ),
                                                )}
                                            </span>
                                        </div>
                                    </Link>
                                ))}
                            </div>
                        </section>
                    </div>

                    {/* 🟢 내가 쓴 댓글 */}
                    <div className="min-w-0 flex-1">
                        <section className="flex flex-col gap-6">
                            <Title label="내가 쓴 댓글" to="current" />
                            <div className="flex min-h-[313px] flex-col gap-4">
                                {displayedComment.map((item) => (
                                    <Link
                                        key={item.boardTeamInfo.boardId}
                                        to={`/team/${item.boardTeamInfo.teamId}/board/${item.boardTeamInfo.teamBoardId}/post/${item.boardTeamInfo.boardId}`}
                                        className="flex gap-4"
                                    >
                                        <img
                                            src={item.imageUri}
                                            className="h-[100px] w-[100px] rounded-xl border border-gray-200 object-cover"
                                        />
                                        <div className="flex min-w-0 flex-1 flex-col justify-between py-2">
                                            <div className="flex flex-col gap-1">
                                                <span className="truncate text-xl font-bold">
                                                    {item.commentContent}
                                                </span>
                                                <span>
                                                    {
                                                        item.boardTeamInfo
                                                            .boardTitle
                                                    }
                                                </span>
                                            </div>
                                            <span>
                                                {dateParsing(
                                                    new Date(item.createdAt),
                                                )}
                                            </span>
                                        </div>
                                    </Link>
                                ))}
                            </div>
                        </section>
                    </div>
                </div>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default MyPage;

import BasicInputModal from '@/components/molecules/BasicInputModal/BasicInputModal';
import useCharge from '@/hooks/useCharge';
import { getMyAllInfoAPI } from '@/apis/AuthAPI';
import { useQuery } from '@tanstack/react-query';
import BaseLayout from './layouts/BaseLayout';
import BodyLayout_24 from './layouts/BodyLayout_24';
import { Icon, Title } from '@/components/atoms';
import { dateParsing } from '@/utils';
import { Link } from 'react-router-dom';
import useMyMileage from '@/hooks/useMyMileage';
import { MileageContainer } from '@/components/organisms';
import { useEffect, useState } from 'react';
import { QRCodeCanvas } from 'qrcode.react';
import { getUserQRCodeAPI } from '@/apis/QRCodeAPI';
import UserInfoUpdateModal from '@/components/molecules/BasicInputModal/UserInfoUpdateModal';

const MyPage = () => {
    const { isOpen, handleConfirm, handleCharge, handleCancel } = useCharge();
    const { myMileageData } = useMyMileage();
    const [accountNumber, setAccountNumber] = useState<string>('');
    const [qrOpen, setQrOpen] = useState<boolean>(false);
    const [QRuuid, setQRuuid] = useState<string>('');
    const [isUserInfoUpdateModalOpen, setUserInfoUpdateModalOpen] =
        useState<boolean>(false);

    const { data } = useQuery({
        queryKey: ['myAllData'],
        queryFn: getMyAllInfoAPI,
        staleTime: 1000 * 30,
        gcTime: 1000 * 60,
    });

    useEffect(() => {
        if (data) {
            setAccountNumber(data.accountNumber);
        }
    }, [data]);

    useEffect(() => {
        const fetchData = async () => {
            const data = await getUserQRCodeAPI({ accountNumber });

            setQRuuid(data);
        };

        fetchData();
    }, [accountNumber]);

    // 글과 댓글 전체 배열을 그대로 사용
    const displayedBoard = Array.isArray(data?.userBoard) ? data.userBoard : [];
    const displayedComment = Array.isArray(data?.userComment)
        ? data.userComment
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
                    <button
                        type="button"
                        onClick={() => setUserInfoUpdateModalOpen(true)}
                    >
                        <div className="absolute right-0 bottom-0 rounded-full bg-white p-2 shadow-md">
                            <Icon id="Pen" type="svg" />
                        </div>
                    </button>
                </div>
                <div className="flex flex-col items-center gap-2">
                    <label className="text-lg font-bold">
                        {data?.nickname}
                    </label>
                    <div className="font-light">{data?.email}</div>
                </div>

                <div className="flex flex-col items-end gap-4">
                    <div className="flex gap-3">
                        <button
                            type="button"
                            className="bg-brand-primary-300 hover:bg-brand-primary-500 w-fit cursor-pointer rounded p-2 text-white"
                            onClick={() => {
                                setQrOpen(!qrOpen);
                            }}
                        >
                            결제하기
                        </button>
                        <button
                            type="button"
                            className="bg-brand-primary-300 hover:bg-brand-primary-500 w-fit cursor-pointer rounded p-2 text-white"
                            onClick={handleCharge}
                        >
                            충전하기
                        </button>
                    </div>
                    <div
                        onClick={(e: React.MouseEvent<HTMLDivElement>) => {
                            setQrOpen(false);
                        }}
                        className={`fixed inset-0 flex items-center justify-center bg-gray-600/20 ${qrOpen ? 'block' : 'hidden'}`}
                    >
                        {QRuuid ? (
                            <QRCodeCanvas
                                value={`${import.meta.env.VITE_APP_URL}pay/${QRuuid}/100/${accountNumber}/바나나우유`}
                                onClick={(e) => {
                                    e.stopPropagation();
                                }}
                            />
                        ) : (
                            <div className="bg-white p-4">
                                <span>QR 코드 데이터가 없습니다..</span>
                            </div>
                        )}
                    </div>
                    <MileageContainer
                        items={myMileageData}
                        titleActive={false}
                    />
                    <BasicInputModal
                        isOpen={isOpen}
                        title="충전 금액을 입력해주세요."
                        subTitle="100원 이상부터 충전 가능합니다."
                        inputPlaceholder="금액을 입력하세요."
                        confirmLabel="충전하기"
                        onConfirmClick={handleConfirm}
                        onCancelClick={handleCancel}
                    />
                    <UserInfoUpdateModal
                        isOpen={isUserInfoUpdateModalOpen}
                        title="내 정보 수정"
                        namePlaceholder="이름 입력"
                        nicknamePlaceholder="닉네임 입력"
                        initialName={data?.name || ''}
                        initialNickname={data?.nickname || ''}
                        initialProfileUrl={data?.profileUri || ''}
                        onConfirm={(updatedData) => {
                            console.log('업데이트된 사용자 정보:', updatedData);
                            // updateUserInfo API 호출 또는 기타 작업 수행 후 모달 닫기
                            setUserInfoUpdateModalOpen(false);
                        }}
                        onCancel={() => {
                            // 모달 닫기
                            setUserInfoUpdateModalOpen(false);
                        }}
                    />
                </div>

                {/* 내가 쓴 글과 댓글을 동등한 크기로 유지하는 컨테이너 */}
                <div className="flex w-full gap-4">
                    {/* 내가 쓴 글 */}
                    <div className="min-w-0 flex-1">
                        <section className="overflow flex flex-col gap-6">
                            <Title label="내가 쓴 글" />
                            <div className="flex max-h-[400px] min-h-[400px] flex-col gap-4 truncate overflow-y-scroll">
                                {displayedBoard.map((item) => {
                                    // boardTeamInfo가 없으면 렌더링하지 않음
                                    if (!item.boardTeamInfo) return null;

                                    return (
                                        <Link
                                            key={item.boardTeamInfo.boardId}
                                            to={`/team/${item.boardTeamInfo.teamId}/board/${item.boardTeamInfo.teamBoardId}/post/${item.boardTeamInfo.boardId}`}
                                            className="flex gap-4"
                                        >
                                            <img
                                                src={item.imageUri}
                                                alt="게시글 이미지"
                                                className="h-[100px] w-[100px] rounded-xl border border-gray-200 object-cover"
                                            />
                                            <div className="flex min-w-0 flex-1 flex-col justify-between py-2">
                                                <div className="flex flex-col gap-1">
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
                                    );
                                })}
                            </div>
                        </section>
                    </div>

                    {/* 내가 쓴 댓글 */}
                    <div className="min-w-0 flex-1">
                        <section className="flex flex-col gap-6">
                            <Title label="내가 쓴 댓글" />
                            <div className="flex max-h-[400px] min-h-[400px] flex-col gap-4 truncate overflow-y-scroll">
                                {displayedComment.map((item) => {
                                    // boardTeamInfo가 없으면 렌더링하지 않음
                                    if (!item.boardTeamInfo) return null;

                                    return (
                                        <Link
                                            key={item.boardTeamInfo.boardId}
                                            to={`/team/${item.boardTeamInfo.teamId}/board/${item.boardTeamInfo.teamBoardId}/post/${item.boardTeamInfo.boardId}`}
                                            className="flex gap-4"
                                        >
                                            <img
                                                src={item.imageUri}
                                                alt="댓글 이미지"
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
                                                        new Date(
                                                            item.createdAt,
                                                        ),
                                                    )}
                                                </span>
                                            </div>
                                        </Link>
                                    );
                                })}
                            </div>
                        </section>
                    </div>
                </div>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default MyPage;

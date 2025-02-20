import { createTeamInstallmentAPI } from '@/apis/IntsallmentAPI';
import { ProfileImage } from '@/components/atoms';
import { useModalStore } from '@/stores/modalStore';
import { InstallmentRequest } from '@/types/Transaction';
import { useState } from 'react';

export interface InstallmentProps {
    key: number;
    nickname: string;
    selected: boolean;
    profileUrl: string;
    userId: string;
    transferRequest: null;
}

interface TeamInstalmentModalProps {
    teamId: string;
    round: string;
    members: InstallmentProps[];
    clickClose: (value: React.SetStateAction<boolean>) => void;
}

const TeamInstallmentModal = ({
    teamId,
    round,
    members,
    clickClose,
}: TeamInstalmentModalProps) => {
    const { openModal, closeModal } = useModalStore();
    const [selectedMembers, setSelectedMembers] = useState<
        InstallmentRequest[]
    >([]);
    const [amount, setAmount] = useState<string>('');

    const createInstallment = () => {
        if (selectedMembers.length == 0) {
            openModal({
                title: '선택한 인원이 없습니다다.',
                onCancelClick: () => {
                    closeModal();
                },
            });
        }

        if (Number(amount) < 1000) {
            openModal({
                title: '회비는 1000원이상부터 가능합니다.',
                onCancelClick: () => {
                    setAmount('');
                    closeModal();
                },
            });
        }

        if (Number.isNaN(Number(amount))) {
            openModal({
                title: '금액은 숫자로 입력해주세요.',
                onCancelClick: () => {
                    setAmount('');
                    closeModal();
                },
            });
        }

        const data = {
            teamId,
            installmentRequests: selectedMembers,
        };

        const fetchData = async () => {
            console.log('요청 데이터', data);

            try {
                const response = await createTeamInstallmentAPI(data);

                if (response) {
                    openModal({
                        title: '회비 생성을 성공했습니다.',
                        onConfirmClick: () => {
                            clickClose(false);
                            closeModal();
                        },
                    });
                }
            } catch (error) {
                console.error(error);
            }
        };

        fetchData();
    };

    const handleClose = () => {
        setSelectedMembers([]);
        clickClose(false);
    };

    if (!members) {
        return;
    }

    return (
        <div
            className="flex h-[50%] w-[50%] flex-col gap-3 rounded-sm bg-white p-4"
            onClick={(e) => {
                e.stopPropagation();
            }}
        >
            <h4 className="text-text text-xl font-bold">팀원 목록</h4>
            <ul className="flex-1">
                {members.map((item) => {
                    return (
                        <li
                            key={item.key}
                            onClick={() => {
                                if (
                                    selectedMembers.some(
                                        (member) =>
                                            member.userId == item.userId,
                                    )
                                ) {
                                    setSelectedMembers(
                                        selectedMembers.filter(
                                            (member) =>
                                                member.userId != item.userId,
                                        ),
                                    );
                                    return;
                                }

                                setSelectedMembers([
                                    ...selectedMembers,
                                    {
                                        teamId,
                                        userId: item.userId,
                                        round,
                                        amount,
                                        transferRequest: null,
                                    },
                                ]);
                            }}
                            className={`flex cursor-pointer items-center gap-3 rounded-sm p-4 ${selectedMembers.some((member) => member.userId == item.userId) ? 'bg-brand-primary-100' : 'bg-white'} hover:bg-brand-primary-200`}
                        >
                            <ProfileImage
                                nickname={item.nickname}
                                profileUri={item.profileUrl}
                                size={30}
                            />
                            <span>{item.nickname}</span>
                        </li>
                    );
                })}
            </ul>
            <div className="ml-auto flex items-center gap-2 bg-gray-50">
                <span>현재 {round} 회차</span>
                <div className="flex gap-2 border border-gray-200 p-3">
                    <label htmlFor="">금액</label>
                    <input
                        type="text"
                        id="amount"
                        name="amount"
                        value={amount}
                        onChange={(e) => {
                            setAmount(e.target.value);
                        }}
                    />
                </div>
            </div>
            <div className="ml-auto flex gap-4">
                <button
                    type="button"
                    onClick={createInstallment}
                    className="bg-brand-primary-200 hover:bg-brand-primary-400 rounded-sm p-3 text-white"
                >
                    생성하기
                </button>
                <button
                    type="button"
                    onClick={handleClose}
                    className="bg-brand-primary-200 hover:bg-brand-primary-400 rounded-sm p-3 text-white"
                >
                    취소
                </button>
            </div>
        </div>
    );
};

export default TeamInstallmentModal;

import { useEffect, useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Outlet, useParams, useNavigate } from 'react-router-dom';
import { getTeamInfo } from '@/apis/TeamAPI';
import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import tagFormatter from '@/utils/tagFormatter';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { getAlbumImageList } from '@/apis/TeamBoardAPI';
import BasicModal from '@/components/molecules/BasicModal/BasicModal';

const TeamLayout = () => {
    const { teamId } = useParams() as { teamId: string };
    const navigate = useNavigate();
    const [isErrorModalOpen, setIsErrorModalOpen] = useState<boolean>(false);

    const { data: albumData } = useQuery({
        queryKey: ['albumdata', teamId],
        queryFn: () => getAlbumImageList(teamId),
    });

    const { data: teamData, error: teamError } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId),
    });

    const { data: myProfileData } = useMyTeamProfile(teamId);

    const formattedTags = tagFormatter(teamData?.tags || []);

    // 에러 발생 시 모달을 띄웁니다.
    useEffect(() => {
        if (teamError) {
            setIsErrorModalOpen(true);
        }
    }, [teamError]);

    const handleErrorModalConfirm = () => {
        setIsErrorModalOpen(false);
        navigate('/');
    };

    return (
        <>
            <BasicModal
                isOpen={isErrorModalOpen}
                title="비공개 모임"
                subTitle="비공개 모임입니다. 메인 페이지로 이동합니다."
                onConfirmClick={handleErrorModalConfirm}
            />
            <main className="w-full">
                <section className="w-full">
                    <img
                        src={teamData?.profileUri}
                        alt={teamId}
                        className="h-[22.5rem] w-full object-cover"
                    />
                </section>
                <section className="flex w-full py-8">
                    <section className="flex w-[29rem] flex-col gap-12 pl-4">
                        <MeetingInfo
                            teamId={teamId}
                            subTitle={'subtitle이 없습니다.'}
                            reviewScore={teamData?.reviewScore || 0}
                            reviewCount={teamData?.reviewCount || 0}
                            title={teamData?.name || 'title이 없습니다.'}
                            tag={formattedTags}
                            maxCapacity={teamData?.maxCapacity || 0}
                            currentCapacity={teamData?.currentCapacity || 0}
                            teamUserId={
                                myProfileData?.teamUserId !== undefined
                                    ? myProfileData.teamUserId
                                    : null
                            }
                            recruitStatus={
                                teamData?.recruitStatus || 'ACTIVE_PUBLIC'
                            }
                            notificationStatus={
                                myProfileData?.notificationStatus || 'ACTIVE'
                            }
                        />
                        <Album
                            id={teamId ?? ''}
                            images={albumData?.images ?? []}
                        />
                    </section>
                    <section className="flex w-full flex-col gap-2 overflow-hidden pl-12">
                        <DetailNav />
                        <Outlet />
                    </section>
                </section>
            </main>
        </>
    );
};

export default TeamLayout;

import { getTeamInfo } from '@/apis/TeamAPI';
import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { Outlet, useParams } from 'react-router-dom';
import tagFormatter from '@/utils/tagFormatter';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { getAlbumImageList } from '@/apis/TeamBoardAPI';
import { useEffect, useState } from 'react';
import { AlbumItemProps } from '@/components/molecules/Album/Album.view';

const TeamLayout = () => {
    const { teamId } = useParams() as { teamId: string };

    const [items, setItems] = useState<AlbumItemProps[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getAlbumImageList(teamId)
            .then(setItems) // ✅ items가 배열이므로 그대로 전달 가능
            .catch((error) => console.error(error))
            .finally(() => setLoading(false));
    }, [teamId]);
    const { data: teamData } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId),
    });

    const { data: myProfileData } = useMyTeamProfile(teamId);

    const formattedTags = tagFormatter(teamData?.tags || []);
    if (loading) return <p>Loading...</p>;
    return (
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
                        rating={{
                            rating: teamData?.score || 0,
                            reviewCount: 0,
                        }}
                        title={teamData?.name || 'title이 없습니다.'}
                        tag={formattedTags}
                        maxCapacity={teamData?.maxCapacity || 0}
                        currentCapacity={teamData?.currentCapacity || 0}
                        teamUserId={
                            myProfileData?.teamUserId !== undefined
                                ? myProfileData.teamUserId
                                : null
                        }
                        nickName="일단닉네임"
                        recruitStatus={
                            teamData?.recruitStatus || 'ACTIVE_PUBLIC'
                        }
                        notificationStatus={
                            myProfileData?.notificationStatus || 'ACTIVE'
                        }
                    />
                    <Album id={teamId ?? ''} items={items} />
                </section>
                <section className="flex w-full flex-col gap-2 overflow-hidden pl-12">
                    <DetailNav />
                    <Outlet />
                </section>
            </section>
        </main>
    );
};

export default TeamLayout;

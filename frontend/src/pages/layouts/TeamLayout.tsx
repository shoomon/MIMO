import { getMyTeamProfile, getTeamInfo } from '@/apis/TeamAPI';
import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { Outlet, useParams } from 'react-router-dom';
import { TeamDataMockup } from '@/mock/TeamLayoutMock';
import tagFormatter from '@/utils/tagFormatter';

const TeamLayout = () => {
    const { teamId } = useParams() as { teamId: string };

    const album = TeamDataMockup;

    const { data: teamData } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId),
    });

    const { data: myProfileData } = useQuery({
        queryKey: ['ProfileInfo', teamId],
        queryFn: () => getMyTeamProfile(teamId),
    });

    const formattedTags = tagFormatter(teamData?.tags || []);

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
                        subTitle={
                            teamData?.description || 'subtitle이 없습니다.'
                        }
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
                    <Album id={teamId ?? ''} items={album.items} />
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

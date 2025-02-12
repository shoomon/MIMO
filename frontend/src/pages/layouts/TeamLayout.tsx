import { getTeamInfo } from '@/apis/TeamAPI';
import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { Outlet, useParams } from 'react-router-dom';
import { TeamDataMockup } from '@/mock/TeamLayoutMock';
import tagFormatter from '@/utils/tagFormatter';

const TeamLayout = () => {
    const { teamId } = useParams() as { teamId: string };

    const album = TeamDataMockup;

    const { data } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId),
    });

    const formattedTags = tagFormatter(data?.tag || []);

    return (
        <main className="w-full">
            <section className="w-full">
                <img
                    src={data?.profileUri}
                    alt={teamId}
                    className="h-[22.5rem] w-full object-cover"
                />
            </section>
            <section className="flex py-8">
                <section className="flex w-[29rem] flex-col gap-12 pl-4">
                    <MeetingInfo
                        teamId={teamId}
                        subTitle={data?.description || 'subtitle이 없습니다.'}
                        rating={{
                            rating: data?.score || 0,
                            reviewCount: 0,
                        }}
                        title={data?.name || 'title이 없습니다.'}
                        tag={formattedTags}
                        maxCapacity={data?.maxCapacity || 0}
                        currentCapacity={data?.currentCapacity || 0}
                    />
                    <Album id={teamId ?? ''} items={album.items} />
                </section>
                <section className="flex w-full flex-col gap-2 pr-4 pl-12">
                    <DetailNav />
                    <Outlet />
                </section>
            </section>
        </main>
    );
};

export default TeamLayout;

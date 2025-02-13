import { getTeamInfo } from '@/apis/TeamAPI';
import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import { useQuery } from '@tanstack/react-query';
import { Outlet, useParams } from 'react-router-dom';
import { TeamDataMockup } from '@/mock/TeamLayoutMock';
import tagFormatter from '@/utils/tagFormatter';
import { typeChecker } from '@/utils/typeChecker';

const TeamLayout = () => {
    const { teamId } = useParams() as { teamId: string };

    const album = TeamDataMockup;

    const { data } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId),
    });

    function isDefined<T>(data: T | null | undefined): data is T {
        return data !== null && data !== undefined;
    }

    if (!isDefined(data)) {
        throw new Error('데이터가 존재하지 않습니다.');
    }

    const formattedTags = tagFormatter(data?.tags || []);

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
                        subTitle={data?.description}
                        rating={{
                            rating: data?.score || 0,
                            reviewCount: 0,
                        }}
                        title={data?.name}
                        tag={formattedTags}
                        maxCapacity={data?.maxCapacity || 0}
                        currentCapacity={data?.currentCapacity || 0}
                        teamUserId={
                            data?.teamUserId !== undefined
                                ? data.teamUserId
                                : null
                        }
                        nickName="일단닉네임"
                        recruitStatus={data?.recruitStatus}
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

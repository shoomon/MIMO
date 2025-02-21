import { getMyTeamInfos } from '@/apis/TeamAPI';
import { CardMeeting } from '@/components/molecules';
import { useAuth } from '@/hooks/useAuth';
import { SimpleTeamResponse } from '@/types/Team';
import tagFormatter from '@/utils/tagFormatter';
import { useQuery } from '@tanstack/react-query';
import BaseLayout from '../layouts/BaseLayout';
import { Title } from '@/components/atoms';

const MyMeeting = () => {
    const { userInfo } = useAuth();

    const { data: MyTeamList } = useQuery({
        queryKey: ['MyTeamList', userInfo?.nickname],
        queryFn: () => getMyTeamInfos(),
        enabled: userInfo !== undefined,
    });

    if (!MyTeamList || MyTeamList.size === 0) {
        return (
            <BaseLayout>
                <Title label="나의 모임" />
                <div className="flex flex-1 flex-col items-center justify-center p-4 text-center">
                    <h1 className="text-3xl font-bold">
                        아직 가입한 모임이 없습니다.
                    </h1>
                </div>
            </BaseLayout>
        );
    }

    const myMeetingList =
        MyTeamList?.teams?.map((item: SimpleTeamResponse) => {
            const formattedTags = tagFormatter(item.tags);
            return (
                <div key={item.teamId} className="w-full">
                    <CardMeeting
                        label={item.name}
                        content={item.description}
                        reviewScore={item.reviewScore}
                        reviewCount={item.reviewCount}
                        tagList={formattedTags}
                        image={{
                            memberCount: item.currentCapacity,
                            memberLimit: item.maxCapacity,
                            imgSrc: item.teamProfileUri,
                            showMember: true,
                        }}
                        to={`/team/${item.teamId}`}
                    />
                </div>
            );
        }) ?? [];

    return (
        <BaseLayout>
            <Title label="나의 모임" />
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 sm:gap-6 md:grid-cols-3 md:gap-8 lg:grid-cols-4 lg:gap-10">
                {myMeetingList}
            </div>
        </BaseLayout>
    );
};

export default MyMeeting;

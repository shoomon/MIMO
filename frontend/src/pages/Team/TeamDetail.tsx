import { Title } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import ButtonLayout from '../layouts/ButtonLayout';
import { useQuery } from '@tanstack/react-query';
import { getTeamInfo } from '@/apis/TeamAPI';
import { useParams } from 'react-router-dom';

const TeamDetail = () => {
    const { teamId } = useParams<{ teamId: string }>();

    const { data: teamData } = useQuery({
        queryKey: ['teamInfo', teamId],
        queryFn: () => getTeamInfo(teamId!),
        staleTime: 1000 * 60 * 5,
    });

    return (
        <BaseLayout>
            <ButtonLayout>
                <div></div>
            </ButtonLayout>
            <BodyLayout_24>
                <Title label="🚀모임 소개🚀" />

                <div className="flex w-full flex-col gap-6">
                    <span className="text-xl font-medium">
                        {teamData?.description}
                    </span>
                </div>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default TeamDetail;

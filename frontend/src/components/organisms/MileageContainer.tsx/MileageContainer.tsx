import { TeamMileageMain } from '@/mock/TeamMileageMock';
import MileageContainerView from './MileageContainer.view';

interface MileageContainerProps {
    teamId: string;
}

const MileageContainer = ({ teamId }: MileageContainerProps) => {
    console.log(teamId);
    const data = TeamMileageMain;

    return <MileageContainerView items={data} />;
};

export default MileageContainer;

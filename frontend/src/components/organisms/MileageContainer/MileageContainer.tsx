import { TeamMileageMain } from '@/mock/TeamMileageMock';
import MileageContainerView from './MileageContainer.view';
import { useParams } from 'react-router-dom';

const MileageContainer = () => {
    const { teamId } = useParams();
    console.log(teamId);
    const data = TeamMileageMain;

    return <MileageContainerView items={data} />;
};

export default MileageContainer;

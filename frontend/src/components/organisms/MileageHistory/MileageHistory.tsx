import { rawTableData } from '@/mock/TeamMileageMock';
import MileageHistoryView from './MileageHistory.view';
import { useParams } from 'react-router-dom';

const MileageHistory = () => {
    const { teamId } = useParams();
    console.log(teamId);

    const data = rawTableData;

    return <MileageHistoryView items={data} />;
};

export default MileageHistory;

import { MileageContainer, MileageHistory } from '@/components/organisms';
import useTeamMileage from '@/hooks/useTeamMileage';

const TeamMileage = () => {
    const { teamMileageData } = useTeamMileage();

    return (
        <div className="flex flex-col gap-16 px-8 py-4">
            <MileageContainer items={teamMileageData} />
            <MileageHistory />
        </div>
    );
};

export default TeamMileage;

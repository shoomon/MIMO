import { MileageContainer, MileageHistory } from '@/components/organisms';

const TeamMileage = () => {
    return (
        <div className="flex flex-col gap-16 px-8 py-4">
            <MileageContainer />
            <MileageHistory />
        </div>
    );
};

export default TeamMileage;

import { Title } from '@/components/atoms';
import MileageStatus, {
    MileageStatusProps,
} from '@/components/atoms/MileageStatus/MileageStatus';

export interface MileageContainerViewProps {
    titleActive?: boolean;
    items: MileageStatusProps[];
}

const MileageContainerView = ({
    items,
    titleActive = true,
}: MileageContainerViewProps) => {
    return (
        <section className="flex flex-col gap-6">
            {titleActive && <Title label="마일리지 💰" />}
            <div className="flex gap-6">
                {items.map((item) => {
                    return (
                        <MileageStatus
                            key={item.type}
                            type={item.type}
                            amount={item.amount}
                        />
                    );
                })}
            </div>
        </section>
    );
};

export default MileageContainerView;

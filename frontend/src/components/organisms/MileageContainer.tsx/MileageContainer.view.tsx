import { Title } from '@/components/atoms';
import MileageStatus, {
    MileageStatusProps,
} from '@/components/atoms/MileageStatus/MileageStatus';

interface MileageContainerViewProps {
    items: MileageStatusProps[];
}

const MileageContainerView = ({ items }: MileageContainerViewProps) => {
    return (
        <section className="flex flex-col gap-6">
            <Title label="마일리지 💰" />
            <div className="flex gap-6">
                {items.map((item) => {
                    return (
                        <MileageStatus type={item.type} amount={item.amount} />
                    );
                })}
            </div>
        </section>
    );
};

export default MileageContainerView;

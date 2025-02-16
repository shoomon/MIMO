import { Title } from '@/components/atoms';
import BaseLayout from '../layouts/BaseLayout';
import BodyLayout_24 from '../layouts/BodyLayout_24';
import ButtonLayout from '../layouts/ButtonLayout';

const TeamDetail = () => {
    return (
        <BaseLayout>
            <ButtonLayout>
                <div></div>
            </ButtonLayout>
            <BodyLayout_24>
                <div className="flex w-full flex-col gap-6">
                    <Title label="🚀나중에 시간이 된다면 타이틀 / desc로 나눠서 쓰면 예쁠듯🚀" />
                    <></>
                </div>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default TeamDetail;

import { ButtonDefault, Title } from '@/components/atoms';
import BodyLayout_24 from '../layouts/BodyLayout_24';

const TeamScheduleCreate = () => {
    return (
        <section className="flex flex-col gap-2">
            <div className="py- flex min-h-[43px] items-end justify-end self-stretch">
                <ButtonDefault
                    content="일정 취소"
                    type="fail"
                    onClick={() => leaveMutation.mutate()}
                />

                <ButtonDefault
                    content="일정 등록"
                    iconId="PlusCalendar"
                    iconType="svg"
                    onClick={() => joinMutation.mutate()}
                />
            </div>

            <BodyLayout_24>
                <Title label="일정 등록" />
            </BodyLayout_24>
        </section>
    );
};

export default TeamScheduleCreate;

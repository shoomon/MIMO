import { Album, DetailNav, MeetingInfo } from '@/components/molecules';
import { TeamDataMockup } from '@/mock/TeamLayoutMock';
import { Outlet, useParams } from 'react-router-dom';

const TeamLayout = () => {
    const { teamId } = useParams();
    const data = TeamDataMockup;

    return (
        <main className="w-full">
            <section className="w-full">
                <img
                    src="https://image.lawtimes.co.kr/images/121732.jpg"
                    alt=""
                    className="h-[22.5rem] w-full object-cover"
                />
            </section>
            <section className="flex py-8">
                <section className="flex w-[28rem] flex-col gap-12 pl-4">
                    <MeetingInfo
                        subTitle={data.subTitle}
                        rating={data.rating}
                        title={data.title}
                        tag={data.tag}
                        member={data.member}
                    />
                    <Album id={teamId ?? ''} items={data.items} />
                </section>
                <section className="flex w-full flex-col gap-2 px-4">
                    <DetailNav />
                    <Outlet />
                </section>
            </section>
        </main>
    );
};

export default TeamLayout;

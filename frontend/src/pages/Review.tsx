import { RatingStar, Title } from '@/components/atoms';
import BaseLayout from './layouts/BaseLayout';
import BodyLayout_24 from './layouts/BodyLayout_24';
import ButtonLayout from './layouts/ButtonLayout';
import { useQuery } from '@tanstack/react-query';
import { getTeamReview } from '@/apis/UserAPI';
import { useParams } from 'react-router-dom';
import useMyTeamProfile from '@/hooks/useMyTeamProfile';
import { InputForm } from '@/components/molecules';
import { review } from '@/types/User';
import { dateParsing } from '@/utils';

const Review = () => {
    const { teamId } = useParams<{ teamId: string }>();
    const { data: profileData } = useMyTeamProfile(teamId);

    const { data } = useQuery({
        queryKey: ['teamReview', teamId],
        queryFn: () => getTeamReview(teamId!),
        enabled: !!teamId,
    });

    if (!data) {
        return (
            <div className="flex h-screen items-center justify-center text-center text-gray-500">
                리뷰가 없습니다.
            </div>
        );
    }

    const reviews = data.reviews.map((item: review) => {
        const date = dateParsing(new Date(item.createAt));
        return (
            <div
                key={item.teamReviewId}
                className="rounded-lg bg-white p-4 shadow-md transition-shadow duration-300 hover:shadow-xl"
            >
                <div className="mb-2 flex items-center">
                    <RatingStar reviewScore={item.score} />
                    <span className="ml-4 text-sm text-gray-600">{date}</span>
                </div>
                <p className="text-gray-800">{item.memo}</p>
            </div>
        );
    });

    return (
        <BaseLayout>
            <ButtonLayout>
                <></>
            </ButtonLayout>
            <BodyLayout_24>
                <section className="flex flex-col gap-8">
                    {!profileData?.hasReview && (
                        <div className="rounded-lg bg-gradient-to-r from-blue-500 to-teal-400 p-4 shadow-md">
                            <InputForm
                                id="review"
                                type="text"
                                placeholder="리뷰를 작성해주세요"
                                label="리뷰 작성"
                            />
                        </div>
                    )}
                    <Title label="팀 리뷰 목록" />
                    <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                        {reviews}
                    </div>
                </section>
            </BodyLayout_24>
        </BaseLayout>
    );
};

export default Review;

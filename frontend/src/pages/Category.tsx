import CategoryList from '@/components/molecules/CategoryList/CategoryList';
import ListContainer from '@/components/organisms/ListContainer';
import { useLocation } from 'react-router-dom';
import { CardMeeting } from '@/components/molecules';
import { TeamAPI } from '@/apis';
import { CardMeetingProps } from '@/components/molecules/CardMeeting/CardMeeting';
import { SimpleTeamResponse } from './../types/Team';
import { Title } from '@/components/atoms';

const Category = () => {
    const location = useLocation();
    const category = location.pathname.split('/').pop() || '';
    //   const { data, isLoading, error } = TeamAPI.getTeamInfosByCategory(category);

    // API 호출 대신 목업 데이터 사용

    const mockData = {
        size: 3,
        hasNext: false,
        lastTeamId: 3,
        teams: [
            {
                teamId: 1,
                name: '주말 자전거 모임',
                description: '매주 주말 한강에서 자전거 타는 모임입니다.',
                teamProfileUri: 'https://picsum.photos/200',
                rating: 4.5,
                reviewCount: 12,
                memberCount: 8,
                memberLimit: 10,
                thumbnail: 'https://picsum.photos/200',
                tags: ['자전거', '운동', '주말'],
            },
            {
                teamId: 2,
                name: '독서토론 모임',
                description: '월 1회 독서토론을 진행합니다.',
                teamProfileUri: 'https://picsum.photos/201',
                rating: 3,
                reviewCount: 15,
                memberCount: 12,
                memberLimit: 15,
                thumbnail: 'https://picsum.photos/201',
                tags: ['독서', '토론', '취미'],
            },
            {
                teamId: 3,
                name: '코딩 스터디',
                description: '주 2회 알고리즘 문제를 풉니다.',
                teamProfileUri: 'https://picsum.photos/202',
                rating: 4.2,
                reviewCount: 8,
                memberCount: 5,
                memberLimit: 6,
                thumbnail: 'https://picsum.photos/202',
                tags: ['개발', '알고리즘', '스터디'],
            },
        ],
    };

    const { data, isLoading, error } = {
        data: mockData as SimpleTeamResponse,
        isLoading: false,
        error: null,
    };

    const size = mockData.teams.length;

    interface SimpleTeamResponse {
        teamId: number;
        name: string;
        description: string;
        teamProfileUri: string;
        rating: number;
        reviewCount: number;
        memberCount: number;
        memberLimit: number;
        thumbnail: string;
        tags: string[];
    }

    const CategoryMeetingList = data?.teams.map((item: SimpleTeamResponse) => {
        const formattedTags = item.tags.map((tag) => ({
            label: tag,
            to: `search/tags`,
        }));

        return (
            <CardMeeting
                key={item.teamId}
                label={item.name}
                content={item.description}
                rating={item.rating}
                tagList={formattedTags}
                reviewCount={item.reviewCount}
                image={{
                    memberCount: item.memberCount,
                    memberLimit: item.memberLimit,
                    imgSrc: item.thumbnail,
                    showMember: true,
                }}
                to={`/meeting/${item.teamId}`}
                // 필요한 경우 추가 props 매핑
            />
        );
    });

    return (
        <>
            <>
                <CategoryList />
                <div className="py-8">
                    <div className="mb-4 text-xl font-bold">
                        {category} 관련 모임 {size}개가 검색되었습니다.
                    </div>
                    <div className="grid grid-cols-1 gap-4 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
                        {CategoryMeetingList ?? <div>모임이 없습니다.</div>}
                    </div>
                </div>
            </>
        </>
    );
};

export default Category;

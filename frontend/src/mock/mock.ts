export interface SimpleTeamResponse {
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

export const mockTeamData: SimpleTeamResponse = {
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
            rating: 4.8,
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

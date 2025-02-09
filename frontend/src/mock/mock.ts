export interface SimpleTeamResponseMok {
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

export const CardMeetingmockData = {
    size: 3,
    hasNext: false,
    lastTeamId: 3,
    teams: [
        {
            teamId: 1,
            name: '서울 독서 모임',
            description: '매주 토요일 강남에서 모여 책을 읽습니다.',
            teamProfileUri: 'https://picsum.photos/200',
            rating: 4.5,
            reviewCount: 12,
            memberCount: 8,
            memberLimit: 10,
            thumbnail: 'https://picsum.photos/200',
            tags: ['독서', '서울', '주말'],
        },
        {
            teamId: 2,
            name: '강남 러닝 크루',
            description: '매일 아침 러닝하는 모임입니다.',
            teamProfileUri: 'https://picsum.photos/201',
            rating: 4.8,
            reviewCount: 15,
            memberCount: 12,
            memberLimit: 15,
            thumbnail: 'https://picsum.photos/201',
            tags: ['운동', '러닝', '아침'],
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

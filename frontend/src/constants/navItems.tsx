export const MAIN_NAV_ITEMS = [
    { value: '탐색', path: '/search' },
    { value: '나의 모임', path: '/mymeeting' },
];

export const DETAIL_NAV_ITEMS = [
    { item: '모임소개', icon: 'Megaphone', link: '.' },
    { item: '일정', icon: 'Calendar', link: './schedule' },
    { item: '게시판', icon: 'Board', link: './board' },
    { item: '멤버', icon: 'ThreePeople', link: './members' },
    { item: '마일리지', icon: 'Coin', link: './mileage' },
];

export const ROUTER_CONFIG: Record<string, { name: string }> = {
    main: { name: '홈' },
    team: { name: '팀' },
    board: { name: '게시판' },
    edit: { name: '수정' },
};

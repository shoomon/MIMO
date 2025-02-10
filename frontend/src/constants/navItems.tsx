export const MAIN_NAV_ITEMS = [
    { value: '탐색', path: '/search' },
    { value: '나의 모임', path: '/meeting' },
    { value: '이모저모', path: '/anything' },
    { value: '이벤트', path: '/event' },
];

export const DETAIL_NAV_ITEMS = [
    { item: '모임소개', icon: 'Megaphone', link: '/introduce' },
    { item: '일정', icon: 'Calendar', link: '/schedule' },
    { item: '게시판', icon: 'Board', link: '/board' },
    { item: '멤버', icon: 'ThreePeople', link: '/member' },
    { item: '마일리지', icon: 'Coin', link: '/mileage' },
];

export const ROUTER_CONFIG: Record<string, { name: string }> = {
    main: { name: '홈' },
    team: { name: '팀' },
    board: { name: '게시판' },
    edit: { name: '수정' },
};

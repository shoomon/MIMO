import ChatContainerView from './ChatContainer.view';

const ChatContainer = () => {
    const data = [
        {
            chatroomId: 2,
            chatroomImage:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chatroomName: '안양 2030 동네친구모임',
            lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
            lastDateTime: '2025년 01월 26일 오후 1:48',
            unreadCount: 21,
        },
        {
            chatroomId: 2,
            chatroomImage:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chatroomName: '안양 2030 동네친구모임',
            lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
            lastDateTime: '2025년 01월 26일 오후 1:48',
            unreadCount: 21,
        },
        {
            chatroomId: 2,
            chatroomImage:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chatroomName: '안양 2030 동네친구모임',
            lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
            lastDateTime: '2025년 01월 26일 오후 1:48',
            unreadCount: 21,
        },
    ];

    return (
        <ChatContainerView
            chatListItems={data}
            chatroomName={null}
            chatroomImage={
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg'
            }
        />
    );
};

export default ChatContainer;

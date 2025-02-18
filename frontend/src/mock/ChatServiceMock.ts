import { ChatItemProps } from "@/components/atoms/ChatItem/ChatItem.view";

  export const CHAT_LIST_DATA = [
        {
            chatroomId: 1,
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
            chatroomId: 3,
            chatroomImage:
                'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
            chatroomName: '안양 2030 동네친구모임',
            lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
            lastDateTime: '2025년 01월 26일 오후 1:48',
            unreadCount: 21,
        },
        {
          chatroomId: 4,
          chatroomImage:
              'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
          chatroomName: '안양 2030 동네친구모임',
          lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
          lastDateTime: '2025년 01월 26일 오후 1:48',
          unreadCount: 21,
      },{
        chatroomId: 5,
        chatroomImage:
            'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
        chatroomName: '안양 2030 동네친구모임',
        lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
        lastDateTime: '2025년 01월 26일 오후 1:48',
        unreadCount: 21,
    },{
      chatroomId: 6,
      chatroomImage:
          'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
      chatroomName: '안양 2030 동네친구모임',
      lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
      lastDateTime: '2025년 01월 26일 오후 1:48',
      unreadCount: 21,
  },{
    chatroomId: 7,
    chatroomImage:
        'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
    chatroomName: '안양 2030 동네친구모임',
    lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
    lastDateTime: '2025년 01월 26일 오후 1:48',
    unreadCount: 21,
},{
  chatroomId: 8,
  chatroomImage:
      'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
  chatroomName: '안양 2030 동네친구모임',
  lastChat: '내일 아침에 같이 모여서 모각코 때릴 사람',
  lastDateTime: '2025년 01월 26일 오후 1:48',
  unreadCount: 21,
},
    ];

    export const CHAT_DATA: ChatItemProps[] = [
        {
            type: 'sender',
            item: {
                id: '12',
                nickname: '박성문',
                profileImageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                chat: '딥시크 짱짱',
                timestamp: '2025-02-11T19:00:45.7557546',
                chatType: 'MESSAGE',
            },
            hasReceivedMessage: false,
        },
        {
            type: 'sender',
            item: {
                id: '13',
                nickname: '박성문',
                profileImageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                chat: '지피티 짱짱',
                timestamp: '2025-02-11T19:00:45.7557546',
                chatType: 'MESSAGE',
            },
            hasReceivedMessage: true,
        },
        {
            type: 'sender',
            item: {
                id: '14',
                nickname: '박성문',
                profileImageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                chat: '클로드 짱짱',
                timestamp: '2025-02-11T19:00:45.7557546',
                chatType: 'MESSAGE',
            },
            hasReceivedMessage: true,
        },
        {
            type: 'receiver',
            item: {
                id: '15',
                nickname: '박성문',
                profileImageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                chat: '퍼플렉시티 짱짱',
                timestamp: '2025-02-11T19:00:45.7557546',
                chatType: 'MESSAGE',
            },
            hasReceivedMessage: false,
        },
        {
            type: 'sender',
            item: {
                id: '16',
                nickname: '박성문',
                profileImageUri:
                    'https://cloudfront-ap-northeast-1.images.arcpublishing.com/chosun/2TKUKXYMQF7ASZEUJLG7L4GM4I.jpg',
                chat: '박성문 짱짱',
                timestamp: '2025-02-11T19:00:45.7557546',
                chatType: 'MESSAGE',
            },
            hasReceivedMessage: false,
        },
    ];
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AppRoutes from './AppRoutes';
import { useModalStore } from './stores/modalStore';
import BasicModal from './components/molecules/BasicModal/BasicModal';
import { useChatStore } from './stores/chatStore';
import { ChatModal } from './components/organisms';

function App() {
    const queryClient = new QueryClient();
    const { isOpen, modalProps } = useModalStore();
    const { isChatOpen } = useChatStore();

    return (
        <QueryClientProvider client={queryClient}>
            <BasicModal
                isOpen={isOpen}
                title={modalProps?.title ?? ''}
                subTitle={modalProps?.subTitle}
                onConfirmClick={modalProps?.onConfirmClick}
                onDeleteClick={modalProps?.onDeleteClick}
                onCancelClick={modalProps?.onCancelClick}
            />
            <ChatModal isChatOpen={isChatOpen} type={'list'} />
            <div className="mx-auto w-full max-w-[1440px]">
                <AppRoutes />
            </div>
        </QueryClientProvider>
    );
}

export default App;

import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AppRoutes from './AppRoutes';
import { useModalStore } from './stores/modalStore';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import BasicModal from './components/molecules/BasicModal/BasicModal';

const queryClient = new QueryClient();

function App() {
    const { isOpen, modalProps, closeModal } = useModalStore();

    return (
        <QueryClientProvider client={queryClient}>
            <BasicModal
                isOpen={isOpen}
                title={modalProps?.title || ''}
                subTitle={modalProps?.subTitle || ''}
                confirmLabel={modalProps?.confirmLabel || '확인'}
                cancelLabel={modalProps?.cancelLabel || '취소'}
                onDeleteClick={modalProps?.onDeleteClick}
                onConfirmClick={modalProps?.onConfirmClick}
                onCancelClick={modalProps?.onCancelClick || closeModal}
            />
            <div className="mx-auto flex h-screen w-full max-w-[1440px] flex-col px-4 pb-4">
                <AppRoutes />
            </div>
            <ReactQueryDevtools initialIsOpen={false} position="left" />
        </QueryClientProvider>
    );
}

export default App;

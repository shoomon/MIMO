import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AppRoutes from './AppRoutes';
import { useModalStore } from './stores/modalStore';
import BasicModal from './components/molecules/BasicModal/BasicModal';

const queryClient = new QueryClient();

function App() {
    const { isOpen, modalProps } = useModalStore();

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
            <div className="mx-auto flex w-full max-w-[1440px] flex-col px-4 pb-4">
                <AppRoutes />
            </div>
        </QueryClientProvider>
    );
}

export default App;

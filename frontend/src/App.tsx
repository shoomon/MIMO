import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import AppRoutes from './AppRoutes';

const queryClient = new QueryClient();

function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <div className="mx-auto w-full max-w-[1440px]">
                <AppRoutes />
            </div>
        </QueryClientProvider>
    );
}

export default App;

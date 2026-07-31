import { QueryClient } from '@tanstack/react-query'

export const queryClient = new QueryClient({

    defaultOptions: {

        queries: {

            retry: 1,                       // Retry failed requests once
            refetchOnWindowFocus: false,    // Don't refetch when returning to the tab
            staleTime: 1000 * 60 * 5        // Cache is considered fresh for 5 minutes
        }
    }
});


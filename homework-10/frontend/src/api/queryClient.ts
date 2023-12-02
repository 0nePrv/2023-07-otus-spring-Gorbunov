import {QueryClient} from "react-query";

const client: QueryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 3
    },
  }
})

export default client
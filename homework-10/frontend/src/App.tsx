import React from "react"
import styles from "./App.module.css";
import {Routing} from "./routing/Routing";
import {QueryClientProvider} from "react-query";
import client from "./api/queryClient";

function App() {
  return (
      <div className={styles.App}>
        <QueryClientProvider client={client}>
          <Routing/>
        </QueryClientProvider>
      </div>
  )
}

export default App;

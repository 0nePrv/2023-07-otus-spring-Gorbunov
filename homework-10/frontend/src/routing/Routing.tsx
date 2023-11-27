import React from "react"
import {BrowserRouter, Navigate, Route, Routes} from 'react-router-dom';
import styles from "../App.module.css";
import {Navbar} from "../ui/Navbar";
import {AuthorRoutes, BookRoutes, GenreRoutes} from "./routes";

export const Routing = () => {
  return (
      <div className={styles.App}>
        <BrowserRouter>
          <Navbar/>
          <Routes>
            <Route path={'/'} element={<Navigate to={'/book'}/>}/>
            <Route path={'/book/*'} element={<BookRoutes/>}/>
            <Route path={'/author/*'} element={<AuthorRoutes/>}/>
            <Route path={'/genre/*'} element={<GenreRoutes/>}/>
          </Routes>
        </BrowserRouter>
      </div>
  );
};

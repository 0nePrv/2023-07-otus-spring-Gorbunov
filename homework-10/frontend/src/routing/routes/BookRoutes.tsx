import React from "react";
import {Route, Routes} from "react-router-dom";
import {CreatePageWrapper, EditPageWrapper, ListPageWrapper, NotFoundPage} from "../PageWrappers";
import {bookResource, commentResource} from "../config";

export const BookRoutes = () => {
  return (
      <Routes>
        <Route path="/" element={<ListPageWrapper resource={bookResource}/>}/>
        <Route path="/edit/:id" element={<EditPageWrapper resource={bookResource}/>}/>
        <Route path="/new" element={<CreatePageWrapper resource={bookResource}/>}/>

        <Route path="/:bookId/comment"
               element={<ListPageWrapper resource={commentResource}/>}/>
        <Route path="/:bookId/comment/edit/:id"
               element={<EditPageWrapper resource={commentResource}/>}/>
        <Route path="/:bookId/comment/new"
               element={<CreatePageWrapper resource={commentResource}/>}/>

        <Route path="/*" element={<NotFoundPage/>}/>
      </Routes>
  );
};

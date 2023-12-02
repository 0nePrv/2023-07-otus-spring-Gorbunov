import {CreatePageWrapper, EditPageWrapper, ListPageWrapper, NotFoundPage} from "../PageWrappers";
import {Route, Routes} from "react-router-dom";
import {authorResource} from "../config";

export const AuthorRoutes = () => {
  return (
      <Routes>
        <Route path={'/'} element={<ListPageWrapper resource={authorResource}/>}/>
        <Route path={'/edit/:id'} element={<EditPageWrapper resource={authorResource}/>}/>
        <Route path={'/new'} element={<CreatePageWrapper resource={authorResource}/>}/>
        <Route path={'/*'} element={<NotFoundPage/>}/>
      </Routes>
  );
};

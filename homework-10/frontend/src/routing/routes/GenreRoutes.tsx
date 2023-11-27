import {CreatePageWrapper, EditPageWrapper, ListPageWrapper, NotFoundPage} from "../PageWrappers";
import {Route, Routes} from "react-router-dom";
import {genreResource} from "../config";

export const GenreRoutes = () => {
  return (
      <Routes>
        <Route path={'/'} element={<ListPageWrapper resource={genreResource}/>}/>
        <Route path={'/edit/:id'} element={<EditPageWrapper resource={genreResource}/>}/>
        <Route path={'/new'} element={<CreatePageWrapper resource={genreResource}/>}/>
        <Route path={'/*'} element={<NotFoundPage/>}/>
      </Routes>
  );
};


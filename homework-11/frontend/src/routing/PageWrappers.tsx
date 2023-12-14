import {ListPage} from "../pages/list";
import {EditPage} from "../pages/edit";
import {CreatePage} from "../pages/create";
import React from "react";
import {Loading} from "../ui/Loading";

const ListPageWrapper = ({resource}) => (
    <React.Suspense fallback={<Loading/>}>
      <ListPage resource={resource}/>
    </React.Suspense>
)

const EditPageWrapper = ({resource}) => (
    <React.Suspense fallback={<Loading/>}>
      <EditPage resource={resource}/>
    </React.Suspense>
)

const CreatePageWrapper = ({resource}) => (
    <React.Suspense fallback={<Loading/>}>
      <CreatePage resource={resource}/>
    </React.Suspense>
)

const NotFoundPage = () => <h1>Page not found</h1>

export {ListPageWrapper, EditPageWrapper, CreatePageWrapper, NotFoundPage}
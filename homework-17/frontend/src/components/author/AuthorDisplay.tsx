import '../../styles/item.css'
import React, {FC} from 'react';
import {DisplayProps} from "../../types/pageFormTypes";
import {Author} from "../../types/domainTypes";

export const AuthorDisplay : FC<DisplayProps<Author>> = ({obj: author}) => {
  return (
      <div key={author.id}>
        <p className={"name"}>{author.name}</p>
      </div>
  )
}
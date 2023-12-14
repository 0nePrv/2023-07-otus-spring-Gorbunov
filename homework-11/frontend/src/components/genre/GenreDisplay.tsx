import React, {FC} from "react"
import '../../styles/item.css'
import {DisplayProps} from "../../types/pageFormTypes";
import {Genre} from "../../types/domainTypes";

export const GenreDisplay: FC<DisplayProps<Genre>> = ({obj: genre}) => {
  return (
      <div key={genre.id}>
        <p className={"name"}>{genre.name}</p>
      </div>
  )
}
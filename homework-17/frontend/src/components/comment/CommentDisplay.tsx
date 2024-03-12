import React, {FC} from "react"
import '../../styles/item.css'
import {DisplayProps} from "../../types/pageFormTypes";
import {Comment} from "../../types/domainTypes";

export const CommentDisplay : FC<DisplayProps<Comment>> = ({obj: comment}) => {
  return (
      <div key={comment.id}>
        <p>{comment.text}</p>
      </div>
  )
}
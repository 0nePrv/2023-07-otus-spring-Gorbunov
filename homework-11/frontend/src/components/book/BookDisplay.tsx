import React, {FC} from "react"
import {useNavigate} from "react-router-dom";
import '../../styles/item.css'
import '../../styles/button.css'
import {DisplayProps} from "../../types/pageFormTypes";
import {Book} from "../../types/domainTypes";

export const BookDisplay: FC<DisplayProps<Book>> = ({obj: book}) => {

  const navigate = useNavigate();

  return (
      <div className={"book-item"} key={book.id}>
        <div className={"info"}>
          <p className={"name"}>{book.name}</p>
          <p>{book?.authorName}</p>
          <p>{book?.genreName}</p>
        </div>
        <button className={"button"} onClick={() => navigate(`/book/${book.id}/comment`)}>
          See comments
        </button>
      </div>
  )
}
import {AuthorForm} from "../components/author/AuthorForm";
import {AuthorDisplay} from "../components/author/AuthorDisplay";
import {BookForm} from "../components/book/BookForm";
import {BookDisplay} from "../components/book/BookDisplay";
import {CommentForm} from "../components/comment/CommentForm";
import {CommentDisplay} from "../components/comment/CommentDisplay";
import {GenreForm} from "../components/genre/GenreForm";
import {GenreDisplay} from "../components/genre/GenreDisplay";
import {Resource, ResourceType} from "../types/resourceTypes";
import {Author, Book, Comment, DomainType, Genre} from "../types/domainTypes";

const authorResource: Resource<Author> = {
  type: ResourceType.Author,
  name: 'Authors',
  Form: AuthorForm,
  Display: AuthorDisplay
}

const bookResource: Resource<Book> = {
  type: ResourceType.Book,
  name: 'Books',
  Form: BookForm,
  Display: BookDisplay
}

const commentResource: Resource<Comment> = {
  type: ResourceType.Comment,
  name: 'Comments',
  Form: CommentForm,
  Display: CommentDisplay
}

const genreResource: Resource<Genre> = {
  type: ResourceType.Genre,
  name: 'Genres',
  Form: GenreForm,
  Display: GenreDisplay
}

const navBarResources: Resource<DomainType>[] = [bookResource, authorResource, genreResource]

export {authorResource, genreResource, commentResource, bookResource, navBarResources}

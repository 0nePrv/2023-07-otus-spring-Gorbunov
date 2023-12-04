export interface Book {
  id: number;
  name: string;
  authorId: string;
  authorName?: string;
  genreId: string;
  genreName?: string;
}

export interface Genre {
  id: string;
  name: string;
}

export interface Author {
  id: string;
  name: string;
}

export interface Comment {
  id: string;
  text: string;
  bookId: string;
}

export type DomainType = Book | Comment | Author | Genre
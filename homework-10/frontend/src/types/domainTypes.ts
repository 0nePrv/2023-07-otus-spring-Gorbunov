export interface Book {
  id: number;
  name: string;
  authorId: number;
  authorName?: string;
  genreId: number;
  genreName?: string;
}

export interface Genre {
  id: number;
  name: string;
}

export interface Author {
  id: number;
  name: string;
}

export interface Comment {
  id: number;
  text: string;
  bookId: number;
}

export type DomainType = Book | Comment | Author | Genre
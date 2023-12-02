import {DomainType} from "./domainTypes";
import {FC} from "react";
import {DisplayProps, FormProps} from "./pageFormTypes";

export enum ResourceType {
  Book = 'book',
  Comment = 'comment',
  Author = 'author',
  Genre = 'genre',
}

export interface Resource<T extends DomainType> {
  type: ResourceType,
  name : string,
  Form: FC<FormProps<T>>,
  Display: FC<DisplayProps<T>>
}
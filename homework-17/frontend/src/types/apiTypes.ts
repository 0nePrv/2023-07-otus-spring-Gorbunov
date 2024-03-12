import {Method} from "axios";
import {ResourceType} from "./resourceTypes";
import {DomainType} from "./domainTypes";

export interface LibraryApiProps {
  resource: ResourceType;
  method: Method;
  pathVar?: string;
  params?: object;
  payload?: object;
}

export interface MultiFetchResult<T extends DomainType> {
  data: T[]
}

export interface SingleFetchResult<T extends DomainType> {
  data: T
}

export interface EmptyFetchResult {
  data: null
}

export type ResponseDataType<T extends DomainType> = null | T | T[]

export type FetchResult<T extends DomainType> = MultiFetchResult<T> | SingleFetchResult<T> | EmptyFetchResult
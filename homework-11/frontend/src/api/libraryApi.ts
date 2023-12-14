import axios, {AxiosError, AxiosInstance, AxiosResponse} from 'axios';
import {ApiError} from "../types/errorTypes";
import {
  EmptyFetchResult,
  FetchResult,
  LibraryApiProps,
  MultiFetchResult,
  ResponseDataType,
  SingleFetchResult
} from "../types/apiTypes";
import {DomainType} from "../types/domainTypes";
import {ResourceType} from "../types/resourceTypes";


const instance: AxiosInstance = axios.create()

instance.interceptors.response.use(
    (response: AxiosResponse) => response,
    (error: AxiosError) => {
      const displayError: ApiError = {response: error.response}
      return Promise.reject(displayError)
    }
);

export function libraryApi() {

  async function fetchData<T extends FetchResult<D>, D extends DomainType>(
      {resource, method, pathVar = '', params = {}, payload = {}}: LibraryApiProps): Promise<T> {
    const url = `/api/${resource}${pathVar === '' ? '' : '/' + pathVar}`
    console.log('REQUEST:', method, url, params, payload)
    const response = await instance.request<ResponseDataType<D>>({
      url,
      method,
      params,
      data: payload
    })
    console.log('RESPONSE:', response)
    return {data: response.data} as T
  }

  function getAll<T extends DomainType>(resource: ResourceType, params: object = {}) {
    return fetchData<MultiFetchResult<T>, T>({resource, method: 'GET', params})
  }

  function get<T extends DomainType>(resource: ResourceType, pathVar: string, params: object = {}) {
    return fetchData<SingleFetchResult<T>, T>({resource, method: 'GET', pathVar, params})
  }

  function create<T extends DomainType>(resource: ResourceType, payload: any, params: object = {}) {
    return fetchData<SingleFetchResult<T>, T>({resource, method: 'POST', params, payload})
  }

  function update<T extends DomainType>(resource: ResourceType, pathVar: string, payload: any, params: object = {}) {
    return fetchData<SingleFetchResult<T>, T>({resource, method: 'PUT', pathVar, params, payload})
  }

  function remove(resource: ResourceType, pathVar: string, params: object = {}) {
    return fetchData<EmptyFetchResult, any>({resource, method: 'DELETE', pathVar, params})
  }

  return {create, getAll, get, update, remove}
}

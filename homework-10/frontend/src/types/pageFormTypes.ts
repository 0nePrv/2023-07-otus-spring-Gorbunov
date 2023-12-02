import {DomainType} from "./domainTypes";
import {Resource} from "./resourceTypes";

export interface FormProps<T extends DomainType> {
  obj?: T,
  handleSubmit: (obj: T) => void;
  handleCancel: () => void;
}

export interface DisplayProps<T extends DomainType> {
  obj: T
}

export interface PageProps<T extends DomainType> {
  resource: Resource<T>
}

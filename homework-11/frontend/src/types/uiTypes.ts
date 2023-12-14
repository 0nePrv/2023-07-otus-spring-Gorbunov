import {DomainType} from "./domainTypes";
import {UseFormRegisterReturn} from "react-hook-form";

export interface ActionPanelProps {
  handleUpdate: () => void;
  handleDelete: () => void;
}

export interface SelectFormProps<T extends DomainType> {
  title: string;
  state: string;
  callback: (state: string) => void;
  register: UseFormRegisterReturn;
  errors: string;
  items: T[];
  displayField: string
}

export interface CommonTextInputProps {
  title: string;
  state: string;
  callback: (state: string) => void;
  register: UseFormRegisterReturn;
  errors: string;
}
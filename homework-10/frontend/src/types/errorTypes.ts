export interface ApiError {
  response: ErrorResponse;
}

export interface ErrorResponse {
  status: number;
  statusText: string;
  data?: ErrorDetail;
}

export interface ErrorDetail {
  detail?: string;
  instance?: string;
  errors?: ErrorField[];
}

export interface ErrorField {
  field?: string;
  message?: string;
  rejectedValue?: any;
}

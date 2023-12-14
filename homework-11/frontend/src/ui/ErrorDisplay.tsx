import React from 'react';
import {ApiError, ErrorDetail, ErrorField} from "../types/errorTypes";


export const ErrorDisplay: React.FC<ApiError> = ({response}) => {

  const detail: ErrorDetail = response.data;
  const errors: ErrorField[] = detail?.errors;

  return (
      <div className='error-container'>
        <h2>Error occurred :(</h2>
        <div className={'error-content'}>
          {response?.status && <p><b>Code:</b> {response.status}</p>}
          {response?.statusText && <p><b>Status:</b> {response.statusText}</p>}
          {detail?.detail && <p><b>Detail:</b> {detail.detail}</p>}
          {detail?.instance && <p><b>Instance:</b> {detail.instance}</p>}
          {errors && errors.length !== 0 && <h3>Errors:</h3>}
          {errors && errors.length !== 0 && errors.map((err, index) => (
              <div key={index}>
                <p><b>Message:</b> {err?.message}</p>
                <p><b>Field:</b> {err?.field}</p>
                <p><b>Value:</b> {err?.rejectedValue === '' ? 'empty string' : err.rejectedValue}
                </p>
              </div>
          ))}
        </div>
      </div>
  );
};
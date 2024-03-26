import React, {ChangeEvent} from 'react';
import {CommonTextInputProps} from "../types/uiTypes";

export const TextAreaComponent = ({title, state, callback, register, errors}: CommonTextInputProps) => {

  const initState = state ?? ''

  const onValueChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    callback(e.target.value)
  }

  return (
      <div className="row" id={`${title} text input row`}>
        <label htmlFor={`${title} text area`}>{title}:</label>
        <textarea
            autoComplete='on'
            id={`${title} text area`}
            placeholder={initState}
            value={state}
            onChange={event => onValueChange(event)}
            {...register}
        />
        {errors && <p className={'validation-errors'}>{errors}</p>}
      </div>
  )
}

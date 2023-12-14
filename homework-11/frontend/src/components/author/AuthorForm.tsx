import React, {FC, useEffect} from 'react';
import {TextInputComponent} from '../../ui/TextInputComponent';
import '../../styles/form.css';
import * as yup from 'yup';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import {FormProps} from "../../types/pageFormTypes";
import {Author} from "../../types/domainTypes";

export const AuthorForm: FC<FormProps<Author>> = ({obj: author, handleSubmit, handleCancel}) => {

  const schema = yup.object().shape({
    name: yup
    .string()
    .required('Name is required')
    .min(4, 'Name too short')
    .max(30, 'Name too long'),
  })

  const {
    register,
    handleSubmit: onFormSubmit,
    setValue,
    watch,
    formState
  } = useForm({
    resolver: yupResolver(schema),
  })

  useEffect(() => {
    setValue('name', author?.name ?? '')
  }, [author?.name, setValue])

  const processForm = (formData: Author) => {
    handleSubmit({...author, name: formData.name});
  }

  return (
      <form className={'form-container'}
            onSubmit={onFormSubmit(processForm)}>
        <TextInputComponent
            title={'Name'}
            state={watch('name') ?? ''}
            callback={(name: string) => setValue('name', name)}
            register={register('name')}
            errors={formState.errors.name?.message}
        />
        <div className="row">
          <input type="submit" value="Submit"/>
          <button type="button" onClick={handleCancel}>
            Cancel
          </button>
        </div>
      </form>
  )
}

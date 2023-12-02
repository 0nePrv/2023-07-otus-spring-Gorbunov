import React, {FC, useEffect, useState} from 'react';
import {libraryApi} from '../../api/libraryApi';
import {useQuery} from 'react-query';
import {Loading} from '../../ui/Loading';
import '../../styles/form.css';
import * as yup from 'yup';
import {useForm} from 'react-hook-form';
import {yupResolver} from '@hookform/resolvers/yup';
import {TextAreaComponent} from '../../ui/TextAreaComponent';
import {SelectComponent} from '../../ui/SelectComponent';
import {ErrorDisplay} from "../../ui/ErrorDisplay";
import {FormProps} from "../../types/pageFormTypes";
import {Book, Comment} from "../../types/domainTypes";
import {ResourceType} from "../../types/resourceTypes";
import {ApiError} from "../../types/errorTypes";

export const CommentForm: FC<FormProps<Comment>> = (
    {obj: comment, handleSubmit, handleCancel}) => {

  const {getAll} = libraryApi()

  const [apiError, setApiError] = useState<ApiError>(null);

  const {data: books, isLoading} = useQuery(['getAll', 'book'],
      () => getAll<Book>(ResourceType.Book, {withRelations: false}), {
        onError(apiError: ApiError) {
          setApiError(apiError)
        }
      })

  const schema = yup.object().shape({
    text: yup
    .string()
    .required('Text is required')
    .min(5, 'Text is too short')
    .max(300, 'Text is too long'),
    bookId: yup
    .number()
    .required('Book is required')
    .min(1, 'Book is not chosen')
  })

  const {
    register,
    handleSubmit: onFormSubmit,
    setValue,
    formState,
    watch
  } = useForm({
    resolver: yupResolver(schema)
  })

  useEffect(() => {
    setValue('text', comment?.text ?? '')
    setValue('bookId', comment?.bookId ?? 0)
  }, [comment?.text, comment?.bookId, setValue])

  const processForm = (data: Comment) => {
    handleSubmit({...comment, text: data.text, bookId: data.bookId})
  }

  if (isLoading) {
    return <Loading/>
  }

  if (apiError) {
    return <ErrorDisplay response={apiError.response}/>
  }

  return (
      <form className={'form-container'}
            onSubmit={onFormSubmit(processForm)}>
        <TextAreaComponent
            title={'Text'}
            state={watch('text') ?? ''}
            callback={(text: string) => setValue('text', text)}
            register={register('text')}
            errors={formState.errors.text?.message}
        />
        <SelectComponent<Book>
            title={'Book'}
            state={watch('bookId') ?? 0}
            callback={(id: number) => setValue('bookId', id)}
            register={register('bookId')}
            errors={formState.errors.bookId?.message}
            items={books.data}
            displayField={'name'}
        />
        <div className="row">
          <input type="submit" value="Submit"/>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
  )
}

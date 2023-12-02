import {libraryApi} from "../../api/libraryApi";
import {useQuery} from "react-query";
import '../../styles/form.css'
import * as yup from "yup";
import {useForm} from "react-hook-form";
import {yupResolver} from "@hookform/resolvers/yup";
import React, {FC, useEffect, useState} from "react";
import {Loading} from "../../ui/Loading";
import {TextInputComponent} from "../../ui/TextInputComponent";
import {SelectComponent} from "../../ui/SelectComponent";
import {ErrorDisplay} from "../../ui/ErrorDisplay";
import {FormProps} from "../../types/pageFormTypes";
import {Author, Book, Genre} from "../../types/domainTypes";
import {ResourceType} from "../../types/resourceTypes";
import {ApiError} from "../../types/errorTypes";


export const BookForm: FC<FormProps<Book>> = ({obj: book, handleSubmit, handleCancel}) => {

  const {getAll} = libraryApi()

  const [apiError, setApiError] = useState<ApiError>(null);

  const {
    data: authors,
    isLoading: authorsIsLoading,
  } = useQuery(['getAll', 'author'], () => getAll<Author>(ResourceType.Author),{
    onError(apiError : ApiError) {
      setApiError(apiError)
    }
  })

  const {
    data: genres,
    isLoading: genresIsLoading
  } = useQuery(['getAll', 'genre'], () => getAll<Genre>(ResourceType.Genre),{
    onError(apiError : ApiError) {
      setApiError(apiError)
    }
  })

  const schema = yup.object().shape({
    name: yup
    .string().required('Name is required')
    .min(2, 'Name is too short')
    .max(50, 'Name is too long'),
    authorId: yup.number().required('Author is required')
    .min(1, 'Author is not chosen'),
    genreId: yup.number().required('Genre is required')
    .min(1, 'Genre is not chosen')
  })

  const {
    register,
    handleSubmit: onFormSubmit,
    setValue,
    watch,
    formState
  } = useForm({
    resolver: yupResolver(schema)
  })

  useEffect(() => {
    setValue('name', book?.name ?? '');
    setValue('authorId', book?.authorId ?? 0);
    setValue('genreId', book?.genreId ?? 0);
  }, [book?.name, book?.authorId, book?.genreId, setValue])

  const processForm = (data: Book) => {
    handleSubmit({
      ...book,
      name: data.name,
      authorId: data.authorId,
      genreId: data.genreId
    })
  }

  if (authorsIsLoading || genresIsLoading) {
    return <Loading/>
  }

  if (apiError) {
    return <ErrorDisplay response={apiError.response}/>
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
        <SelectComponent<Author>
            title={'Author'}
            state={watch('authorId') ?? 0}
            callback={(id: number) => setValue('authorId', id)}
            register={register('authorId')}
            errors={formState.errors.authorId?.message}
            items={authors.data}
            displayField={'name'}
        />
        <SelectComponent<Genre>
            title={'Genre'}
            state={watch('genreId') ?? 0}
            callback={(id: number) => setValue('genreId', id)}
            register={register('genreId')}
            errors={formState.errors.genreId?.message}
            items={genres.data}
            displayField={'name'}
        />
        <div className="row">
          <input type="submit" value="Submit"/>
          <button type="button" onClick={handleCancel}>Cancel</button>
        </div>
      </form>
  )
}

import React, {useState} from "react"
import {useNavigate, useParams} from "react-router-dom";
import {useQuery} from "react-query";
import {libraryApi} from "../../api/libraryApi";
import {Loading} from "../../ui/Loading";
import {ErrorDisplay} from "../../ui/ErrorDisplay";
import {DomainType} from "../../types/domainTypes";
import {PageProps} from "../../types/pageFormTypes";
import {ApiError} from "../../types/errorTypes";
import {ResourceType} from "../../types/resourceTypes";

function EditPage<T extends DomainType>({resource}: PageProps<T>) {

  const {type, name, Form} = resource

  const {update, get} = libraryApi()

  const {id} = useParams()

  const navigate = useNavigate()

  const [apiError, setApiError] = useState<ApiError>(null);

  const {data: result, isLoading} = useQuery(['update', type],
      () => get<T>(type, id), {
        onError(apiError: ApiError) {
          setApiError(apiError)
        }
      })

  const onSubmit = async (data: T) => {
    try {
      await update(type, data.id.toString(), data);
      setApiError(null)
      doNavigate(data)
    } catch (updateError) {
      setApiError(updateError)
    }
  }

  const doNavigate = (obj: T) => {
    if (type === ResourceType.Comment) {
      navigate(`/book/${obj['bookId']}/comment`)
    } else {
      navigate(`/${type}`)
    }
  }

  if (isLoading) {
    return <Loading/>
  }

  if (apiError) {
    return <ErrorDisplay response={apiError.response}/>
  }

  return (
      <div>
        <h1>{name} edit page</h1>
        <Form obj={result.data}
              handleSubmit={onSubmit}
              handleCancel={() => doNavigate(result.data)}/>
      </div>
  )
}

export default EditPage
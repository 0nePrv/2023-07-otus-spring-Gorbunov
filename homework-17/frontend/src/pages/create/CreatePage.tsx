import React, {useState} from "react";
import {To, useNavigate, useParams} from "react-router-dom";
import {libraryApi} from "../../api/libraryApi";
import {ErrorDisplay} from "../../ui/ErrorDisplay";
import {DomainType} from "../../types/domainTypes";
import {PageProps} from "../../types/pageFormTypes";
import {ApiError} from "../../types/errorTypes";
import {ResourceType} from "../../types/resourceTypes";

function CreatePage<T extends DomainType>({resource}: PageProps<T>) {

  const {type, name, Form} = resource

  const [apiError, setApiError] = useState<ApiError>(null);

  const navigate = useNavigate()

  const {bookId} = useParams()

  const {create} = libraryApi()

  const onSubmit = async (data: T) => {
    try {
      await create<T>(resource.type, data);
      setApiError(null)
      doNavigate(false, data)
    } catch (error) {
      setApiError(error)
    }
  }

  const onCancel = () => doNavigate(true)

  const doNavigate = (canceled : boolean, data?: T) => {
    let url: To = `/${type}`
    if (type === ResourceType.Comment) {
      if (canceled) {
        url = `/book/${bookId}/comment`
      } else {
        url = `/book/${data['bookId']}/comment`
      }
    }
    navigate(url)
  }


  if (apiError) {
    return <ErrorDisplay response={apiError.response}/>
  }

  return (
      <div>
        <h1>{name} create page</h1>
        <Form handleSubmit={onSubmit} handleCancel={onCancel}/>
      </div>
  )
}

export default CreatePage
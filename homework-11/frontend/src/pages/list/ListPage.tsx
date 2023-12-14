import React, {useEffect, useRef, useState} from "react"
import {useQuery} from "react-query";
import {libraryApi} from "../../api/libraryApi";
import "../../styles/button.css"
import "../../styles/list.css"
import {ActionPanel} from "../../ui/ActionPannel";
import {useNavigate, useParams} from "react-router-dom";
import {Loading} from "../../ui/Loading";
import {ErrorDisplay} from "../../ui/ErrorDisplay";
import {Book, DomainType} from "../../types/domainTypes";
import {PageProps} from "../../types/pageFormTypes";
import {ApiError} from "../../types/errorTypes";
import {ResourceType} from "../../types/resourceTypes";
import {SingleFetchResult} from "../../types/apiTypes";

function ListPage<T extends DomainType>({resource}: PageProps<T>) {

  const {type, name, Display} = resource

  const titleRef = useRef<string>(name);

  const [apiError, setApiError] = useState<ApiError>(null)

  const [bookLoading, setBookLoading] = useState<boolean>(false);

  const {remove, getAll, get} = libraryApi()

  const {bookId} = useParams()

  const navigate = useNavigate()

  const doQuery = async () => {
    let params = {}
    if (type === ResourceType.Comment) {
      params = {bookId}
    }
    return await getAll<T>(type, params)
  }

  const {data: result, refetch, isLoading} = useQuery(['getAll', type],
      doQuery, {
        onError(apiError: ApiError) {
          setApiError(apiError)
        }
      })

  function fetchBook() {
    setBookLoading(true)
    get<Book>(ResourceType.Book, bookId)
    .then((book: SingleFetchResult<Book>) => {
      titleRef.current = `${book.data.name} ${name}` || name
    })
    .catch((e: ApiError) => {
      setApiError(e)
    })
    .finally(() => {
      setBookLoading(false)
    })
  }

  useEffect(() => {
    if (bookId && type === ResourceType.Comment) {
      fetchBook();
    } else {
      titleRef.current = name
    }
  }, [bookId, name, type])

  const onCreate = () => {
    const base = type === ResourceType.Comment ? `/book/${bookId}/comment` : `/${type}`
    navigate(`${base}/new`)
  }

  const onEdit = (obj: T) => {
    const base = type === ResourceType.Comment ? `/book/${obj['bookId']}/comment` : `/${type}`
    navigate(`${base}/edit/${obj.id}`)
  }

  const onDelete = async (obj: T) => {
    try {
      await remove(type, obj.id.toString())
      await refetch()
      setApiError(null)
    } catch (error) {
      setApiError(error)
    }
  }

  const header = <div className={"header"}>
    <h1>{titleRef.current} page</h1>
    <button className={"button"} title={"refresh"} onClick={async () => {
      setApiError(null)
      await refetch()
    }}>
      <img src="/icons/refresh.png" alt={"refresh"}/>
    </button>
    <button className={"button"} title={"add"} onClick={onCreate}>
      <img src={"/icons/plus.png"} alt={"new"}/>
    </button>
  </div>;

  if (isLoading || bookLoading) {
    return <Loading/>
  }

  if (apiError) {
    return (
    <div>
      {header}
      <ErrorDisplay response={apiError.response}/>
    </div>
    )
  }

  return (
      <div>
        {header}
        {(result?.data && result.data?.length !== 0) ?
            (<div className={"container"}>
              {result.data.map(obj =>
                  <div key={obj.id} className={"item"}>
                    <Display obj={obj}/>
                    <ActionPanel handleUpdate={() => onEdit(obj)}
                                 handleDelete={() => onDelete(obj)}/>
                  </div>)}
            </div>) :
            <p className={"notFound"}>{name} not found</p>}
      </div>
  )
}

export default ListPage
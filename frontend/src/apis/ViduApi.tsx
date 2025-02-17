import { customFetch } from "./customFetch"

export const createSessionAPI = async (
  sessionId: string
) => {
  try{

    const response = await customFetch("/openvidu/sessions",{
      method: "POST",
      body: JSON.stringify({customSessionId: sessionId})
    })

    if(!response.ok){
      throw new Error("데이터 실패");
    }

    return response.json();
  }catch(error){
    console.error(error);
  }
}

export const createViduTokenAPI = async (
  sessionId: string
) => {

  try{

    const response = await customFetch(`/openvidu/sessions/${sessionId}/connections`, {
      method: "POST"
    })

    if(!response.ok){
      throw new Error("데이터 실패");
    }

    console.log(sessionId, response.json());

    return response.json();
  }catch(error){

    console.error(error);
  }

}
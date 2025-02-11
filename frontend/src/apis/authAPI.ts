import { customFetch } from "./customFetch";

export const oauthAPI = () => {

    const authURL = `https://accounts.google.com/o/oauth2/auth?client_id=${import.meta.env.VITE_GOOGLE_CLIENT_ID}&redirect_uri=${import.meta.env.VITE_GOOGLE_REDIRECT_URL}&response_type=code&scope=openid%20email%20profile&access_type=offline&prompt=consent`;

    window.open(
      authURL,
      "Google Login",
      "width=500, height=600, left=100, top=100"
    );

}

export const getToken = async (accessToken: string) => {
  const response = await customFetch(
    `${import.meta.env.VITE_GOOGLE_OAUTH_BACKEND_ENDPOINT}?code=${accessToken}`,
    {
      method: "GET",
    }
  )

  console.log(response);
}
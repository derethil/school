import { useEffect, useState } from "react";

export const useAuth = () => {
  const [token, setToken] = useState(
    window.localStorage.getItem("token") || ""
  );

  useEffect(() => {
    window.localStorage.setItem("token", token);
  }, [token]);

  const isLoggedIn = token !== "";

  return { token, setToken, isLoggedIn };
};

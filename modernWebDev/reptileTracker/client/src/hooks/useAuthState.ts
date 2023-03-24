import { useEffect, useMemo, useState } from "react";

export const useAuthState = () => {
  const [token, setToken] = useState(
    window.localStorage.getItem("token") || ""
  );

  useEffect(() => {
    window.localStorage.setItem("token", token);
  }, [token]);

  return { token, setToken };
};

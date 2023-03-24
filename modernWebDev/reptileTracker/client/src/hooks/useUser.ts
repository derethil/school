import { useContext, useEffect, useState } from "react";
import { ApiContext } from "../contexts/api";
import { AuthContext } from "../contexts/auth";
import { UserContext } from "../contexts/user";
import { Api } from "../lib/api";
import { User } from "../types/users";
import { useApi } from "./useApi";

export const useUser = () => {
  return useContext(UserContext);
};

export const useUserState = () => {
  const [user, setUser] = useState<User | null>(null);

  useEffect(() => {
    if (window.localStorage.getItem("token") && !user) {
      const api = new Api(window.localStorage.getItem("token")!);
      api.get("/users/me").then(({ user }) => setUser(user));
    }
  }, [user]);

  return { user, setUser };
};

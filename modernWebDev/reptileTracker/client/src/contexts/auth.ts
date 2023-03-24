import { createContext } from "react";

interface AuthContext {
  token: string;
  setToken: (token: string) => void;
}

const defaultContext: AuthContext = {
  token: "",
  setToken: (token) => {},
};

export const AuthContext = createContext<AuthContext>(defaultContext);

import { createContext } from "react";

interface AuthContext {
  isLoggedIn: boolean;
  setToken: (token: string) => void;
}

const defaultContext: AuthContext = {
  isLoggedIn: false,
  setToken: (token) => {},
};

export const AuthContext = createContext<AuthContext>(defaultContext);

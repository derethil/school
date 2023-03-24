import { useMemo, useState } from "react";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ApiContext } from "./contexts/api";
import { AuthContext } from "./contexts/auth";
import { UserContext } from "./contexts/user";
import { useAuthState } from "./hooks/useAuthState";
import { useUserState } from "./hooks/useUser";
import { Api } from "./lib/api";
import { Dashboard } from "./pages/Dashboard";
import { Home } from "./pages/Home";
import { Login } from "./pages/Login";
import { Register } from "./pages/Register";
import { Reptile } from "./pages/Reptile";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/login",
    element: <Login />,
  },
  {
    path: "/register",
    element: <Register />,
  },
  {
    path: "/dashboard",
    element: <Dashboard />,
  },
  {
    path: "/reptile/:id",
    element: <Reptile />,
  },
]);

export function App() {
  const { token, setToken } = useAuthState();
  const { user, setUser } = useUserState();

  const api = useMemo(() => new Api(token), [token]);

  return (
    <AuthContext.Provider value={{ token, setToken }}>
      <ApiContext.Provider value={api}>
        <UserContext.Provider value={{ user, setUser }}>
          <RouterProvider router={router} />
        </UserContext.Provider>
      </ApiContext.Provider>
    </AuthContext.Provider>
  );
}

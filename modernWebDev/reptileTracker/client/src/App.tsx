import { createBrowserRouter, RouterProvider } from "react-router-dom";
import { ApiContext } from "./contexts/api";
import { AuthContext } from "./contexts/auth";
import { useAuth } from "./hooks/useAuth";
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
  const { token, setToken, isLoggedIn } = useAuth();

  return (
    <AuthContext.Provider value={{ isLoggedIn, setToken }}>
      <ApiContext.Provider value={new Api(token)}>
        <RouterProvider router={router} />
      </ApiContext.Provider>
    </AuthContext.Provider>
  );
}

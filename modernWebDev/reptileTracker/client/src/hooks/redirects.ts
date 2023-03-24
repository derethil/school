import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "./useAuth";

export const useProtectedRoute = () => {
  const navigate = useNavigate();
  const { token } = useAuth();

  useEffect(() => {
    if (!token || token === "") {
      navigate("/", {
        replace: true,
      });
    }
  });
};

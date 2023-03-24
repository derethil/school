import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { PageWrapper } from "../components/PageWrapper";
import { useProtectedRoute } from "../hooks/redirects";
import { useApi } from "../hooks/useApi";
import { useAuth } from "../hooks/useAuth";

export function Dashboard() {
  const navigate = useNavigate();
  const api = useApi();

  useProtectedRoute();

  return <PageWrapper>Dashboard</PageWrapper>;
}

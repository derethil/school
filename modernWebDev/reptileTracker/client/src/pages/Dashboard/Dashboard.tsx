import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { PageWrapper } from "../../components/PageWrapper";
import { useProtectedRoute } from "../../hooks/redirects";
import { useApi } from "../../hooks/useApi";
import { useAuth } from "../../hooks/useAuth";
import { ReptilesList } from "./ReptilesList";
import { Schedules } from "./Schedules";

export function Dashboard() {
  const navigate = useNavigate();
  const api = useApi();

  useProtectedRoute();

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl mb-6">Dashboard</h1>

      <div className="flex  ">
        <div className="mr-8">
          <Schedules />
        </div>

        <div>
          <ReptilesList />
        </div>
      </div>
    </PageWrapper>
  );
}

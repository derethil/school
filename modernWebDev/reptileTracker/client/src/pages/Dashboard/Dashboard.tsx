import { useEffect } from "react";
import { Button } from "react-daisyui";
import { useNavigate } from "react-router-dom";
import { PageWrapper } from "../../components/PageWrapper";
import { useProtectedRoute } from "../../hooks/redirects";
import { useApi } from "../../hooks/useApi";
import { useAuth } from "../../hooks/useAuth";
import { ReptilesList } from "./ReptilesList";
import { Schedules } from "./Schedules";

export function Dashboard() {
  const navigate = useNavigate();
  const { setToken } = useAuth();

  useProtectedRoute();

  const handleLogout = () => {
    setToken("");
    setTimeout(() => navigate("/"), 0);
  };

  return (
    <PageWrapper center={false}>
      <div className="flex justify-between items-start">
        <h1 className="text-4xl mb-6">Dashboard</h1>
        <Button className="btn-primary" onClick={handleLogout}>
          Logout
        </Button>
      </div>

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

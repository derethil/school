import { useContext, useEffect } from "react";
import { Button } from "react-daisyui";
import { useNavigate } from "react-router-dom";
import { PageWrapper } from "../../components/PageWrapper";
import { AuthContext } from "../../contexts/auth";

import { useUser } from "../../hooks/useUser";
import { ReptilesList } from "./ReptilesList";
import { Schedules } from "./Schedules";

export function Dashboard() {
  const { user } = useUser();
  const navigate = useNavigate();
  const { setToken } = useContext(AuthContext);

  const handleLogout = () => {
    setToken("");
    setTimeout(() => navigate("/"), 0);
  };

  // Handle redirecting to login page if not logged in
  if (!window.localStorage.getItem("token")) return <></>;
  useEffect(() => {
    if (!window.localStorage.getItem("token")) {
      navigate("/", {
        replace: true,
      });
    }
  }, []);

  // Render dashboard

  return (
    <PageWrapper center={false}>
      <div className="flex justify-between items-start mb-4">
        <h1 className="text-4xl mb-6">Dashboard</h1>
        <div className="flex items-center">
          <p className="text-sm mr-2">
            {user?.firstName} {user?.lastName}
          </p>
          <Button className="btn-primary" onClick={handleLogout}>
            Logout
          </Button>
        </div>
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

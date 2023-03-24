import { Reptile } from "@prisma/client";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-daisyui";
import { useNavigate } from "react-router-dom";
import { PageWrapper } from "../../components/PageWrapper";
import { AuthContext } from "../../contexts/auth";
import { useApi } from "../../hooks/useApi";

import { useUser } from "../../hooks/useUser";
import { ScheduleWithReptile } from "../../types/reptileState";
import { ReptilesList } from "./ReptilesList";
import { Schedules } from "./Schedules";

const daysOfWeek = [
  "sunday",
  "monday",
  "tuesday",
  "wednesday",
  "thursday",
  "friday",
  "saturday",
] as const;

export function Dashboard() {
  const api = useApi();
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

  // Get schedules (needed there o handle deleting schedules on reptile delete)

  const [schedules, setSchedules] = useState<ScheduleWithReptile[]>([]);

  const fetchSchedules = async () => {
    const { schedules } = (await api.get("/users/me/schedules")) as {
      schedules: ScheduleWithReptile[];
    };
    const todayIndex = new Date().getDay();
    const todaysSchedules = schedules.filter((schedule) => {
      return schedule[daysOfWeek[todayIndex]];
    });
    setSchedules(todaysSchedules);
  };

  useEffect(() => {
    fetchSchedules();
  }, []);

  // Handle Schedule changes depending on reptile actions

  const handleReptileDelete = (id: number) => {
    setSchedules(schedules.filter((schedule) => schedule.reptileId !== id));
  };

  const handleReptileEdit = (updatedReptile: Reptile) => {
    setSchedules(
      schedules.map((schedule) => {
        if (schedule.reptileId === updatedReptile.id) {
          return {
            ...schedule,
            reptile: updatedReptile,
          };
        } else {
          return schedule;
        }
      })
    );
  };

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

      <div className="flex flex-col xl:flex-row">
        <div className="xl:mr-8">
          <Schedules schedules={schedules} />
        </div>

        <div>
          <ReptilesList
            onDelete={handleReptileDelete}
            onEdit={handleReptileEdit}
          />
        </div>
      </div>
    </PageWrapper>
  );
}

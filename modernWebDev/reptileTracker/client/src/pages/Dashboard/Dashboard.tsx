import { Reptile } from "@prisma/client";
import { useContext, useEffect, useState } from "react";
import { Button } from "react-daisyui";
import { useNavigate } from "react-router-dom";
import { Header } from "../../components/Header";
import { PageWrapper } from "../../components/PageWrapper";
import { AuthContext } from "../../contexts/auth";
import { useApi } from "../../hooks/useApi";

import { useUser } from "../../hooks/useUser";
import { ScheduleWithReptile } from "../../types/reptileState";
import { ReptilesList } from "./ReptilesList";
import { Schedules } from "./Schedules";

export const daysOfWeek = [
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
  const navigate = useNavigate();

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
      <Header pageTitle="Dashboard" />

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

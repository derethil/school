import { Schedule } from "@prisma/client";
import { useContext, useEffect, useState } from "react";
import { UserContext } from "../../contexts/user";
import { useApi } from "../../hooks/useApi";

export function Schedules() {
  const { user } = useContext(UserContext);
  const api = useApi();
  const [schedules, setSchedules] = useState<Schedule[]>([]);

  useEffect(() => {
    const fetchSchedules = async () => {
      const response = await api.get("/users/me/schedules");
      setSchedules(response.schedules as Schedule[]);
    };

    fetchSchedules();
  }, []);

  return (
    <div>
      <h1 className="text-2xl w-64">Today's Schedules</h1>
    </div>
  );
}

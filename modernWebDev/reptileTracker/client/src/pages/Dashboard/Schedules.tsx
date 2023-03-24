import { Schedule } from "@prisma/client";
import { useEffect, useState } from "react";
import { useApi } from "../../hooks/useApi";

export function Schedules() {
  const api = useApi();
  const [schedules, setSchedules] = useState<Schedule[]>([]);

  useEffect(() => {
    const fetchSchedules = async () => {
      const response = await api.get("/users/me/schedules");
      setSchedules(response.schedules as Schedule[]);
    };

    fetchSchedules();
  }, []);

  console.log(schedules);

  return (
    <div>
      <h1 className="text-2xl w-64">Today's Schedules</h1>
    </div>
  );
}

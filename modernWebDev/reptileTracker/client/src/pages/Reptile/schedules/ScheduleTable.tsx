import { HusbandryRecord, Schedule } from "@prisma/client";
import { useEffect, useState } from "react";
import { Button, Modal, Table } from "react-daisyui";
import { sentenceCase } from "../../../util/stringCases";
import { ReptileWithExtras } from "../Reptile";
import { CreateSchedule } from "./CreateSchedule";

interface ScheduleTableProps {
  reptile: ReptileWithExtras;
}

export function ScheduleTable(props: ScheduleTableProps) {
  const { reptile: givenReptile } = props;
  const [reptile, setReptile] = useState(givenReptile);

  useEffect(() => {
    setReptile({
      ...givenReptile,
      schedules: reptile.schedules,
    });
  }, [givenReptile]);

  const handleCreateSchedule = (record: Schedule) => {
    setReptile((prev) => ({
      ...prev,
      schedules: [...prev.schedules, record],
    }));
  };

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h1 className="text-2xl">{reptile.name}'s Schedules</h1>
        <CreateSchedule
          reptile={reptile}
          handleCreateSchedule={handleCreateSchedule}
        />
      </div>

      <Table zebra className="w-full">
        <Table.Head>
          <span>Description</span>
          <span>Type</span>
          <span>Days of the Week</span>
        </Table.Head>

        <Table.Body>
          {reptile.schedules.map((schedule: Schedule) => (
            <Table.Row key={schedule.id}>
              <span>{sentenceCase(schedule.type)}</span>
              <span>{schedule.description}</span>
              <span>
                {Object.entries(schedule).map(([key, value]) => {
                  if (key.includes("day") && value) {
                    return key.slice(0, 3) + " ";
                  }
                })}
              </span>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

// Logging in
// The display of the dashboard page showing the list of schedules and reptiles
// Creating a new reptile
// Editing an existing reptile
// Creating a husbandry record for a reptile
// Creating a feeding for a reptile
// Creating a schedule for a reptile
// Deleting a reptile
// Logging out

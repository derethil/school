import { Table } from "react-daisyui";
import { useApi } from "../../hooks/useApi";
import { ScheduleWithReptile } from "../../types/reptileState";
import { sentenceCase } from "../../util/stringCases";

interface SchedulesProps {
  schedules: ScheduleWithReptile[];
}

export function Schedules(props: SchedulesProps) {
  const api = useApi();

  // Render schedules

  return (
    <div>
      <h1 className="text-2xl w-64 mb-4">Today's Tasks</h1>

      <Table className="w-full xl:w-96">
        <Table.Head>
          <div className="xl:w-24">Type</div>
          <div className="xl:w-24">Reptile</div>
          <div className="xl:w-64">Description</div>
        </Table.Head>

        <Table.Body>
          {props.schedules.map((schedule) => (
            <Table.Row key={schedule.id}>
              <div className="">{sentenceCase(schedule.type)}</div>
              <div className="">{schedule.reptile.name}</div>
              <div className="whitespace-normal">{schedule.description}</div>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

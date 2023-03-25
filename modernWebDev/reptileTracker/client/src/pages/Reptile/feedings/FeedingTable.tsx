import { Feeding, HusbandryRecord, Schedule } from "@prisma/client";
import { useEffect, useState } from "react";
import { Button, Modal, Table } from "react-daisyui";
import { sentenceCase } from "../../../util/stringCases";
import { ReptileWithExtras } from "../Reptile";
import { CreateFeeding } from "./CreateFeeding";

interface ScheduleTableProps {
  reptile: ReptileWithExtras;
}

export function FeedingTable(props: ScheduleTableProps) {
  const { reptile: givenReptile } = props;
  const [reptile, setReptile] = useState(givenReptile);

  useEffect(() => {
    setReptile(givenReptile);
  }, [givenReptile]);

  const handleCreateFeeding = (record: Feeding) => {
    setReptile((prev) => ({
      ...prev,
      feedings: [...prev.feedings, record],
    }));
  };

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h1 className="text-2xl">{reptile.name}'s Feedings</h1>
        <CreateFeeding
          reptile={reptile}
          handleCreateFeeding={handleCreateFeeding}
        />
      </div>

      <Table zebra className="w-full">
        <Table.Head>
          <span>Food Item</span>
          <span>Fed On</span>
        </Table.Head>

        <Table.Body>
          {reptile.feedings.map((feeding: Feeding) => (
            <Table.Row key={feeding.id}>
              <span>{sentenceCase(feeding.foodItem)}</span>
              <span>{new Date(feeding.createdAt).toLocaleDateString()}</span>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

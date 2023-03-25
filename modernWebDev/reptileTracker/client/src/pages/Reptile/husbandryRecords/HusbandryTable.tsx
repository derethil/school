import { HusbandryRecord, Reptile } from "@prisma/client";
import { useEffect, useState } from "react";
import { Button, Modal, Table } from "react-daisyui";
import { ReptileWithExtras } from "../Reptile";
import { CreateHusbandryRecord } from "./CreateHusbandryRecord";

interface HusbandryTableProps {
  reptile: ReptileWithExtras;
}

export function HusbandryTable(props: HusbandryTableProps) {
  const { reptile: givenReptile } = props;

  const [reptile, setReptile] = useState(givenReptile);

  useEffect(() => {
    setReptile(givenReptile);
  }, [givenReptile]);

  const handleCreateHusbandryRecord = (record: HusbandryRecord) => {
    setReptile((prev) => ({
      ...prev,
      husbandryRecords: [...prev.husbandryRecords, record],
    }));
  };

  return (
    <div>
      <div className="flex justify-between mb-4">
        <h1 className="text-2xl">{reptile.name}'s Husbandry Records</h1>
        <CreateHusbandryRecord
          reptile={reptile}
          handleCreateHusbandryRecord={handleCreateHusbandryRecord}
        />
      </div>

      <Table zebra className="w-full">
        <Table.Head>
          <span>Humidity</span>
          <span>Length</span>
          <span>Temperature</span>
          <span>Weight</span>
          <span>Recorded On</span>
        </Table.Head>

        <Table.Body>
          {reptile.husbandryRecords.map((record: HusbandryRecord) => (
            <Table.Row key={record.id}>
              <span>{record.humidity}</span>
              <span>{record.length}</span>
              <span>{record.temperature}</span>
              <span>{record.weight}</span>
              <span>{new Date(record.createdAt).toLocaleDateString()}</span>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

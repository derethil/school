import {
  Feeding,
  HusbandryRecord,
  Reptile as ReptileType,
  Schedule,
} from "@prisma/client";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Header } from "../../components/Header";
import { PageWrapper } from "../../components/PageWrapper";
import { useApi } from "../../hooks/useApi";
import { EditReptile } from "../Dashboard/reptileForm/EditReptile";
import { FeedingTable } from "./feedings/FeedingTable";
import { HusbandryTable } from "./husbandryRecords/HusbandryTable";
import { ScheduleTable } from "./schedules/ScheduleTable";

export interface ReptileWithExtras extends ReptileType {
  husbandryRecords: HusbandryRecord[];
  schedules: Schedule[];
  feedings: Feeding[];
}

export function Reptile() {
  const api = useApi();
  const params = useParams();
  const [reptile, setReptile] = useState<ReptileWithExtras | null>(null);

  const fetchReptile = async () => {
    const reptile: ReptileWithExtras = (await api.get(`/reptiles/${params.id}`))
      .reptile;
    setReptile(reptile);
  };

  useEffect(() => {
    fetchReptile();
  }, []);

  if (!reptile) {
    return <></>;
  }

  console.log(reptile);

  return (
    <PageWrapper center={false}>
      <Header pageTitle={reptile.name} />

      <div className="mb-8">
        <HusbandryTable reptile={reptile} />
      </div>

      <div className="mb-8">
        <ScheduleTable reptile={reptile} />
      </div>

      <div className="mb-8">
        <FeedingTable reptile={reptile} />
      </div>

      <div className="flex justify-end mt-4">
        <EditReptile
          reptile={reptile}
          buttonSize="md"
          buttonText="Edit Reptile"
          onEdit={(updatedReptile) => {
            setReptile({ ...reptile, ...updatedReptile });
          }}
        />
      </div>
    </PageWrapper>
  );
}

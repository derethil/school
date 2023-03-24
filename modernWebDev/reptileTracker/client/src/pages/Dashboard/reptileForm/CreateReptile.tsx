import { Reptile } from "@prisma/client";
import { useState } from "react";
import { Button } from "react-daisyui";
import { useApi } from "../../../hooks/useApi";
import { ReptileState } from "../../../types/reptileState";
import { FormModal } from "./FormModal";
import { CreateReptileBody } from "../../../../../dto/reptiles";

interface CreateReptilePRops {
  onCreate?: (reptile: Reptile) => void;
}

interface CreateReptileResponse {
  reptile: Reptile;
}

export function CreateReptile(props: CreateReptilePRops) {
  const api = useApi();
  const [isOpen, setIsOpen] = useState(false);
  const toggleOpen = () => setIsOpen(!isOpen);

  const handleSubmit = async (reptileToUpdate: ReptileState) => {
    const { reptile }: CreateReptileResponse = await api.post(
      `/reptiles`,
      reptileToUpdate as CreateReptileBody
    );

    if (reptile) {
      props.onCreate?.(reptile);
    }
  };

  return (
    <span className="font-sans">
      <Button size="sm" onClick={toggleOpen}>
        Create
      </Button>
      <FormModal
        isOpen={isOpen}
        toggleOpen={toggleOpen}
        handleSubmit={handleSubmit}
      />
    </span>
  );
}

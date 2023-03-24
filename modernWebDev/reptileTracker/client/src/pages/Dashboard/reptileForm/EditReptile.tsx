import { Reptile } from "@prisma/client";
import { useState } from "react";
import { Button } from "react-daisyui";
import { useApi } from "../../../hooks/useApi";
import { ReptileState } from "../../../types/reptileState";
import { FormModal } from "./FormModal";
import { UpdateReptileBody } from "../../../../../dto/reptiles";

interface EditReptileProps {
  reptile: Reptile;
  onEdit?: (reptile: Reptile) => void;
}

interface EditReptileResponse {
  reptile: Reptile;
}

export function EditReptile(props: EditReptileProps) {
  const api = useApi();
  const [isOpen, setIsOpen] = useState(false);
  const toggleOpen = () => setIsOpen(!isOpen);

  const handleSubmit = async (reptileToUpdate: ReptileState) => {
    const { reptile }: EditReptileResponse = await api.put(
      `/reptiles/${props.reptile.id}`,
      reptileToUpdate as UpdateReptileBody
    );

    if (reptile) {
      props.onEdit?.(reptile);
    }
  };

  return (
    <span className="font-sans">
      <Button size="xs" onClick={toggleOpen}>
        Edit
      </Button>
      <FormModal
        reptileToEdit={{
          name: props.reptile.name,
          species: props.reptile.species,
          sex: props.reptile.sex,
        }}
        isOpen={isOpen}
        toggleOpen={toggleOpen}
        handleSubmit={handleSubmit}
      />
    </span>
  );
}

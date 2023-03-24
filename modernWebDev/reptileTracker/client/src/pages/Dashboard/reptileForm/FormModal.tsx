import { Reptile } from "@prisma/client";
import { useState } from "react";
import { Alert, Button, Dropdown, Modal } from "react-daisyui";

import { Input } from "../../../components/Input";
import { Select } from "../../../components/Select";
import { AlertState } from "../../../types/alert";
import { ReptileState } from "../../../types/reptileState";

interface FormModalProps {
  isOpen: boolean;
  toggleOpen: () => void;
  handleSubmit: (reptile: ReptileState) => void;
  reptileToEdit?: ReptileState;
}

const defaultReptile: ReptileState = {
  name: "",
  species: "ball_python",
  sex: "m",
};

export function FormModal(props: FormModalProps) {
  const [alert, setAlert] = useState<AlertState | null>(null);
  const [reptile, setReptile] = useState<ReptileState>(
    props.reptileToEdit || defaultReptile
  );

  const handleSetReptile = (key: keyof ReptileState, value: string) => {
    setReptile((prev) => ({ ...prev, [key]: value }));
  };

  const handleCancel = () => {
    props.toggleOpen();
    setTimeout(() => {
      setReptile(props.reptileToEdit || defaultReptile);
      setAlert(null);
    }, 250);
  };

  const handleSubmit = () => {
    if (!reptile.name) {
      setAlert({ message: "Please enter a name", status: "warning" });
      return;
    }

    setAlert({
      status: "success",
      message: props.reptileToEdit ? "Reptile updated!" : "Reptile added!",
    });

    setTimeout(() => {
      props.toggleOpen();
      setAlert(null);
      props.handleSubmit(reptile);
    }, 1500);
  };

  return (
    <Modal open={props.isOpen}>
      <Modal.Header className="font-bold">
        {props.reptileToEdit ? "Edit Reptile" : "Add Reptile"}
      </Modal.Header>

      <Modal.Body>
        <Input
          className="mb-2"
          label="Name"
          value={reptile.name}
          onChange={(e) => handleSetReptile("name", e.target.value)}
        />

        <Select
          className="mb-2"
          label="Species"
          value={reptile.species}
          onChange={(e) => handleSetReptile("species", e.target.value)}
        >
          <option value="ball_python">Ball Python</option>
          <option value="king_snake">King Snake</option>
          <option value="corn_snake">Corn Snake</option>
          <option value="redtail_boa">Redtail Boa</option>
        </Select>

        <Select
          className="mb-4"
          label="Sex"
          value={reptile.sex}
          onChange={(e) => handleSetReptile("sex", e.target.value)}
        >
          <option value="m">Male</option>
          <option value="f">Female</option>
        </Select>

        {alert && (
          <Alert status={alert.status} className="mb-4">
            {alert.message}
          </Alert>
        )}
      </Modal.Body>

      <Modal.Actions>
        <Button onClick={handleCancel} className="btn-secondary">
          Cancel
        </Button>
        <Button onClick={handleSubmit} className="btn-primary">
          Submit
        </Button>
      </Modal.Actions>
    </Modal>
  );
}

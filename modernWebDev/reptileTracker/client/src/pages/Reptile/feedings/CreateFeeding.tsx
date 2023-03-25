import { Feeding, Reptile } from "@prisma/client";
import { useState } from "react";
import { Alert, Button, Modal } from "react-daisyui";
import { CreateFeedingBody } from "../../../../../dto/reptiles";
import { Input } from "../../../components/Input";
import { useApi } from "../../../hooks/useApi";
import { AlertState } from "../../../types/alert";

interface CreateFeedingProps {
  reptile: Reptile;
  handleCreateFeeding: (record: Feeding) => void;
}

const defaultFeeding: CreateFeedingBody = {
  foodItem: "",
};

export function CreateFeeding(props: CreateFeedingProps) {
  const api = useApi();
  const [isOpen, setIsOpen] = useState(false);
  const toggleOpen = () => setIsOpen(!isOpen);

  const [alert, setAlert] = useState<AlertState | null>(null);
  const [feeding, setFeeding] = useState(defaultFeeding);

  const handleSetFeeding = (key: keyof CreateFeedingBody, value: string) => {
    setFeeding((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async () => {
    if (!feeding.foodItem) {
      setAlert({
        status: "warning",
        message: "Please enter a food item.",
      });
      return;
    }

    const created: Feeding = (
      await api.post(`/reptiles/${props.reptile.id}/feedings`, feeding)
    ).feeding;

    setAlert({
      status: "success",
      message: "Feeding created!",
    });

    setTimeout(() => {
      setAlert(null);
      toggleOpen();
      setFeeding(defaultFeeding);
      props.handleCreateFeeding(created);
    }, 1500);
  };

  const handleCancel = () => {
    toggleOpen();
    setTimeout(() => {
      setFeeding(defaultFeeding);
      setAlert(null);
    }, 500);
  };

  return (
    <div>
      <Button size="sm" onClick={toggleOpen}>
        Create
      </Button>
      <Modal open={isOpen}>
        <Modal.Header className="font-bold mb-4">Create a Feeding</Modal.Header>

        <Modal.Body>
          {alert && (
            <Alert status={alert.status} className="mb-4">
              {alert.message}
            </Alert>
          )}

          <Input
            type="text"
            className="mb-2"
            label="Food Item"
            value={feeding.foodItem}
            onChange={(e) => handleSetFeeding("foodItem", e.target.value)}
          />
        </Modal.Body>

        <Modal.Actions>
          <Button color="secondary" onClick={handleCancel}>
            Cancel
          </Button>
          <Button color="primary" onClick={handleSubmit}>
            Create
          </Button>
        </Modal.Actions>
      </Modal>
    </div>
  );
}

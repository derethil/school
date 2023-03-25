import { HusbandryRecord, Reptile } from "@prisma/client";
import { useState } from "react";
import { Alert, Button, Modal } from "react-daisyui";
import { CreateHusbandryBody } from "../../../../../dto/reptiles";
import { Input } from "../../../components/Input";
import { useApi } from "../../../hooks/useApi";
import { AlertState } from "../../../types/alert";

interface CreateHusbandryRecordProps {
  reptile: Reptile;
  handleCreateHusbandryRecord: (record: HusbandryRecord) => void;
}

const defaultHusbandryRecord: CreateHusbandryBody = {
  humidity: 0,
  length: 0,
  temperature: 0,
  weight: 0,
};

export function CreateHusbandryRecord(props: CreateHusbandryRecordProps) {
  const api = useApi();
  const [isOpen, setIsOpen] = useState(false);
  const toggleOpen = () => setIsOpen(!isOpen);

  const [alert, setAlert] = useState<AlertState | null>(null);
  const [husbandryRecord, setHusbandryRecord] = useState(
    defaultHusbandryRecord
  );

  const handleSetHusbandryRecord = (
    key: keyof typeof husbandryRecord,
    value: string
  ) => {
    setHusbandryRecord((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async () => {
    // check if all fields are numbers
    const allNumbers = Object.values(husbandryRecord).every(
      (value) => isNaN(Number(value)) === false
    );

    if (!allNumbers) {
      setAlert({
        status: "warning",
        message: "All fields must be numbers",
      });
      return;
    }

    const created: HusbandryRecord = (
      await api.post(`/reptiles/${props.reptile.id}/husbandry_records`, {
        humidity: Number(husbandryRecord.humidity),
        length: Number(husbandryRecord.length),
        temperature: Number(husbandryRecord.temperature),
        weight: Number(husbandryRecord.weight),
      })
    ).husbandryRecord;

    setAlert({
      status: "success",
      message: "Husbandry record created!",
    });

    setTimeout(() => {
      setAlert(null);
      toggleOpen();
      setHusbandryRecord(defaultHusbandryRecord);
      props.handleCreateHusbandryRecord(created);
    }, 1500);
  };

  const handleCancel = () => {
    toggleOpen();
    setTimeout(() => {
      setHusbandryRecord(defaultHusbandryRecord);
      setAlert(null);
    }, 500);
  };

  return (
    <div>
      <Button size="sm" onClick={toggleOpen}>
        Create
      </Button>
      <Modal open={isOpen}>
        <Modal.Header className="font-bold mb-4">
          Create a Husbandry Record
        </Modal.Header>

        <Modal.Body>
          {alert && (
            <Alert status={alert.status} className="mb-4">
              {alert.message}
            </Alert>
          )}

          <Input
            type="number"
            className="mb-2"
            label="Humidity"
            value={husbandryRecord.humidity}
            onChange={(e) =>
              handleSetHusbandryRecord("humidity", e.target.value)
            }
          />

          <Input
            type="number"
            className="mb-2"
            label="Length"
            value={husbandryRecord.length}
            onChange={(e) => handleSetHusbandryRecord("length", e.target.value)}
          />

          <Input
            type="number"
            className="mb-2"
            label="Temperature"
            value={husbandryRecord.temperature}
            onChange={(e) =>
              handleSetHusbandryRecord("temperature", e.target.value)
            }
          />

          <Input
            type="number"
            className="mb-2"
            label="Weight"
            value={husbandryRecord.weight}
            onChange={(e) => handleSetHusbandryRecord("weight", e.target.value)}
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

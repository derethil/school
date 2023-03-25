import { Reptile, Schedule } from "@prisma/client";
import { useState } from "react";
import { Alert, Button, Modal } from "react-daisyui";
import { CreateScheduleBody } from "../../../../../dto/reptiles";
import { Checkbox } from "../../../components/Checkbox";
import { Input } from "../../../components/Input";
import { Select } from "../../../components/Select";
import { useApi } from "../../../hooks/useApi";
import { AlertState } from "../../../types/alert";
import { sentenceCase } from "../../../util/stringCases";
import { daysOfWeek } from "../../Dashboard/Dashboard";

interface CreateScheduleProps {
  reptile: Reptile;
  handleCreateSchedule: (record: Schedule) => void;
}

const defaultSchedule: CreateScheduleBody = {
  type: "feed",
  description: "",
  monday: false,
  tuesday: false,
  wednesday: false,
  thursday: false,
  friday: false,
  saturday: false,
  sunday: false,
};

export function CreateSchedule(props: CreateScheduleProps) {
  const api = useApi();
  const [isOpen, setIsOpen] = useState(false);
  const toggleOpen = () => setIsOpen(!isOpen);

  const [alert, setAlert] = useState<AlertState | null>(null);
  const [schedule, setSchedule] = useState(defaultSchedule);

  const handleSetSchedule = (
    key: keyof CreateScheduleBody,
    value: string | boolean
  ) => {
    setSchedule((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async () => {
    const created: Schedule = (
      await api.post(`/reptiles/${props.reptile.id}/schedules`, schedule)
    ).schedule;

    setAlert({
      status: "success",
      message: "Schedule created!",
    });

    setTimeout(() => {
      setAlert(null);
      toggleOpen();
      setSchedule(defaultSchedule);
      props.handleCreateSchedule(created);
    }, 1500);
  };

  const handleCancel = () => {
    toggleOpen();
    setTimeout(() => {
      setSchedule(defaultSchedule);
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
          Create a Schedule
        </Modal.Header>

        <Modal.Body>
          {alert && (
            <Alert status={alert.status} className="mb-4">
              {alert.message}
            </Alert>
          )}

          <Select
            label="Type"
            value={schedule.type}
            onChange={(e) => handleSetSchedule("type", e.target.value)}
            className="mb-2"
          >
            <option value="feed">Feed</option>
            <option value="clean">Clean</option>
            <option value="other">Other</option>
          </Select>

          <Input
            type="text"
            className="mb-2"
            label="Description"
            value={schedule.description}
            onChange={(e) => handleSetSchedule("description", e.target.value)}
          />

          <div className="mb-2">
            <p className="text-lg text-accent-content">Days of the Week</p>
          </div>

          {daysOfWeek.map((day) => {
            return (
              <Checkbox
                className="mb-2"
                label={sentenceCase(day)}
                key={day}
                checked={schedule[day]}
                onChange={() => {
                  handleSetSchedule(day, !schedule[day]);
                }}
              />
            );
          })}
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

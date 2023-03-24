import { useEffect, useState } from "react";
import { Reptile } from "@prisma/client";
import { useApi } from "../../hooks/useApi";
import { Button, Table } from "react-daisyui";
import { snakeToSentence } from "../../util/stringCases";
import { useNavigate } from "react-router-dom";
import { EditReptile } from "./reptileForm/EditReptile";
import { CreateReptile } from "./reptileForm/CreateReptile";

interface ReptilesListProps {
  onDelete?: (id: number) => void;
  onEdit?: (reptile: Reptile) => void;
}

export function ReptilesList(props: ReptilesListProps) {
  const api = useApi();
  const navigate = useNavigate();
  const [reptiles, setReptiles] = useState<Reptile[]>([]);

  const fetchReptiles = async () => {
    const reptiles: Reptile[] = (await api.get("/reptiles")).reptiles;
    setReptiles(reptiles);
  };

  useEffect(() => {
    fetchReptiles();
  }, []);

  const handleDelete = async (id: number) => {
    const response = await api.del(`/reptiles/${id}`);
    setReptiles(reptiles.filter((reptile) => reptile.id !== id));

    if (props.onDelete) {
      props?.onDelete(id);
    }
  };

  const onEdit = (updatedReptile: Reptile) => {
    setReptiles(
      reptiles.map((reptile) => {
        if (reptile.id === updatedReptile.id) {
          return updatedReptile;
        } else {
          return reptile;
        }
      })
    );

    if (props.onEdit) {
      props?.onEdit(updatedReptile);
    }
  };

  return (
    <div>
      <div className="flex justify-between">
        <h1 className="text-2xl mb-4">My Reptiles</h1>
        <CreateReptile
          onCreate={(reptile) => {
            setReptiles([...reptiles, reptile]);
          }}
        />
      </div>

      <Table className="w-screen xl:w-96">
        <Table.Head>
          <div className="xl:w-24">Name</div>
          <div className="xl:w-24">Species</div>
          <div className="xl:w-8">Sex</div>
          <div className="xl:w-48">Actions</div>
        </Table.Head>

        <Table.Body>
          {reptiles.map((reptile) => (
            <Table.Row
              key={reptile.id}
              onClick={(e) => navigate(`/reptile/${reptile.id}`)}
              className="cursor-pointer"
              hover
            >
              <span>{reptile.name}</span>
              <span>{snakeToSentence(reptile.species)}</span>
              <span>{reptile.sex.toLocaleUpperCase()}</span>
              <span className="flex justify-end items-center">
                <Button
                  size="xs"
                  className="btn-neutral mr-2"
                  onClick={(e) => {
                    e.stopPropagation();
                    handleDelete(reptile.id);
                  }}
                >
                  Delete
                </Button>
                <EditReptile reptile={reptile} onEdit={onEdit} />
              </span>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

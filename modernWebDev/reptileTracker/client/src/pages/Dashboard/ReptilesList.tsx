import { useEffect, useState } from "react";
import { Reptile } from "@prisma/client";
import { useApi } from "../../hooks/useApi";
import { Button, Table } from "react-daisyui";
import { snakeToSentence } from "../../util/snakeToSentence";

export function ReptilesList() {
  const api = useApi();
  const [reptiles, setReptiles] = useState<Reptile[]>([]);

  useEffect(() => {
    const fetchReptiles = async () => {
      const reptiles: Reptile[] = (await api.get("/reptiles")).reptiles;
      setReptiles(reptiles);
    };

    fetchReptiles();
  }, []);

  const handleDelete = async (id: number) => {
    const response = await api.del(`/reptiles/${id}`);
    setReptiles(reptiles.filter((reptile) => reptile.id !== id));
  };

  return (
    <div>
      <h1 className="text-2xl w-64 mb-4">My Reptiles</h1>

      <Table className="w-96">
        <Table.Head>
          <span>Name</span>
          <span>Species</span>
          <span>Sex</span>
          <span>Actions</span>
        </Table.Head>

        <Table.Body>
          {reptiles.map((reptile) => (
            <Table.Row key={reptile.id}>
              <span>{reptile.name}</span>
              <span>{snakeToSentence(reptile.species)}</span>
              <span>{reptile.sex.toLocaleUpperCase()}</span>
              <span>
                <Button
                  size="xs"
                  className="btn-neutral"
                  onClick={() => handleDelete(reptile.id)}
                >
                  Delete
                </Button>
              </span>
            </Table.Row>
          ))}
        </Table.Body>
      </Table>
    </div>
  );
}

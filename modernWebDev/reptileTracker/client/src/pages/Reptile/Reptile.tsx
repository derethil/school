import { useParams } from "react-router-dom";

export function Reptile() {
  const params = useParams();

  return <div>Reptile {params.id}</div>;
}

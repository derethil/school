import { Endpoint } from "../dto/controllers";
import { RequestWithJWTBody } from "../dto/jwt";
import { controller } from "../lib/controller";

interface CreateReptileBody {
  species: "ball_python" | "king_snake" | "corn_snake" | "redtail_boa";
  name: string;
  sex: "m" | "f";
}

const createReptile: Endpoint = (deps) => async (req: RequestWithJWTBody, res) => {
  const { client } = deps;
  const userId = req.jwtBody?.userId;
  const { species, name, sex } = req.body as CreateReptileBody;

  const reptile = await client.reptile.create({
    data: {
      user: {
        connect: {
          id: userId,
        },
      },
      species,
      name,
      sex,
    },
  });

  res.json({ reptile });
};

const getReptiles: Endpoint = (deps) => async (req: RequestWithJWTBody, res) => {
  const { client } = deps;
  const userId = req.jwtBody?.userId;

  const reptiles = await client.reptile.findMany({
    where: {
      userId,
    },
  });

  res.json({ reptiles });
};

const deleteReptile: Endpoint = (deps) => async (req: RequestWithJWTBody, res) => {
  const { client } = deps;
  const userId = req.jwtBody?.userId;
  const reptileId = parseInt(req.params.id);

  if (isNaN(reptileId)) {
    res.status(400).json({ error: "invalid request" });
    return;
  }

  const reptile = await client.reptile.findFirst({
    where: {
      userId: userId,
      id: reptileId,
    },
  });

  if (!reptile) {
    res.status(404).json({ error: "reptile not found" });
    return;
  }

  await client.reptile.delete({
    where: {
      id: reptileId,
    },
  });

  res.status(204).send();
};

type UpdateReptileBody = Partial<CreateReptileBody>;

const updateReptile: Endpoint = (deps) => async (req: RequestWithJWTBody, res) => {
  const { client } = deps;
  const userId = req.jwtBody?.userId;
  const reptileId = parseInt(req.params.id);
  const updateData = req.body as UpdateReptileBody;

  if (isNaN(reptileId)) {
    res.status(400).json({ error: "invalid request" });
    return;
  }

  const reptile = await client.reptile.findFirst({
    where: {
      userId: userId,
      id: reptileId,
    },
  });

  if (!reptile) {
    res.status(404).json({ error: "reptile not found" });
    return;
  }

  const updatedReptile = await client.reptile.update({
    where: {
      id: reptileId,
    },
    data: updateData,
  });

  res.json({ reptile: updatedReptile });
};

export const reptilesController = controller("reptiles", [
  { path: "/", method: "post", endpoint: createReptile },
  { path: "/", method: "get", endpoint: getReptiles },
  { path: "/:id", method: "delete", endpoint: deleteReptile },
  { path: "/:id", method: "put", endpoint: updateReptile },
]);

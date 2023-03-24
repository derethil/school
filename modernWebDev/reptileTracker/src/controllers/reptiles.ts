import { Endpoint } from "../dto/controllers";
import { RequestWithJWTBody } from "../dto/jwt";
import { controller } from "../lib/controller";
import { validationMiddleware } from "../middleware/validation";

import {
  CreateFeedingBody,
  CreateHusbandryBody,
  CreateReptileBody,
  CreateScheduleBody,
  UpdateReptileBody,
} from "../../dto/reptiles";

// ====================
// Reptiles
// ====================

const createReptile: Endpoint =
  (deps) => async (req: RequestWithJWTBody, res) => {
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

    res.status(201).json({ reptile });
  };

const getReptiles: Endpoint =
  (deps) => async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;

    const reptiles = await client.reptile.findMany({
      where: {
        userId,
      },
    });

    res.status(200).json({ reptiles });
  };

const deleteReptile: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);

    const reptile = await client.reptile.findFirst({
      where: {
        userId: userId,
        id: reptileId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    await client.reptile.delete({
      where: {
        id: reptileId,
      },
    });

    res.status(200).json({ success: true });
  },
];

const updateReptile: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);
    const updateData = req.body as UpdateReptileBody;

    const reptile = await client.reptile.findFirst({
      where: {
        userId: userId,
        id: reptileId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const updatedReptile = await client.reptile.update({
      where: {
        id: reptileId,
      },
      data: updateData,
    });

    res.status(200).json({ reptile: updatedReptile });
  },
];

// ======================
// Feedings
// ======================

const createFeeding: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);
    const { foodItem } = req.body as CreateFeedingBody;

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const feeding = await client.feeding.create({
      data: {
        reptile: {
          connect: {
            id: reptileId,
          },
        },
        foodItem,
      },
    });

    res.status(201).json({ feeding });
  },
];

const getFeedings: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const feedings = await client.feeding.findMany({
      where: {
        reptileId,
      },
    });

    res.status(200).json({ feedings });
  },
];

// ======================
// Husbandry
// ======================

const createHusbandryRecords: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);
    const { length, weight, temperature, humidity } =
      req.body as CreateHusbandryBody;

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const husbandry = await client.husbandryRecord.create({
      data: {
        reptile: {
          connect: {
            id: reptileId,
          },
        },
        length,
        weight,
        temperature,
        humidity,
      },
    });

    res.status(201).json({ husbandry });
  },
];

const getHusbandryRecords: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const husbandryRecords = await client.husbandryRecord.findMany({
      where: {
        reptileId,
      },
    });

    res.status(200).json({ husbandryRecords });
  },
];

// ======================
// Schedules
// ======================

const createSchedule: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);
    const body = req.body as CreateScheduleBody;

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const schedule = await client.schedule.create({
      data: {
        reptile: {
          connect: {
            id: reptileId,
          },
        },
        user: {
          connect: {
            id: userId,
          },
        },
        ...body,
      },
    });

    res.status(201).json({ schedule });
  },
];

const getSchedules: Endpoint = (deps) => [
  validationMiddleware,
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;
    const reptileId = parseInt(req.params.id);

    const reptile = await client.reptile.findFirst({
      where: {
        id: reptileId,
        userId,
      },
    });

    if (!reptile) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const schedules = await client.schedule.findMany({
      where: {
        reptileId,
      },
    });

    res.json({ schedules });
  },
];

export const reptilesController = controller("reptiles", [
  { path: "/", method: "post", endpoint: createReptile },
  { path: "/", method: "get", endpoint: getReptiles },
  { path: "/:id", method: "delete", endpoint: deleteReptile },
  { path: "/:id", method: "put", endpoint: updateReptile },
  { path: "/:id/feedings", method: "post", endpoint: createFeeding },
  { path: "/:id/feedings", method: "get", endpoint: getFeedings },
  {
    path: "/:id/husbandry_records",
    method: "post",
    endpoint: createHusbandryRecords,
  },
  {
    path: "/:id/husbandry_records",
    method: "get",
    endpoint: getHusbandryRecords,
  },
  { path: "/:id/schedules", method: "post", endpoint: createSchedule },
  { path: "/:id/schedules", method: "get", endpoint: getSchedules },
]);

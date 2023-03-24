import { Endpoint } from "../dto/controllers";
import { controller } from "../lib/controller";
import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import { RequestWithJWTBody } from "../dto/jwt";

import { CreateUserBody } from "../../dto/users";

const getMe: Endpoint = (deps) => async (req: RequestWithJWTBody, res) => {
  const { client } = deps;
  const userId = req.jwtBody?.userId;

  const user = await client.user.findUnique({ where: { id: userId } });
  const { passwordHash: _, ...userWithoutPassword } = user!;

  res.status(200).json({ user: userWithoutPassword });
};

const createUser: Endpoint = (deps) => async (req, res) => {
  const { client } = deps;
  const { firstName, lastName, email, password } = req.body as CreateUserBody;

  const passwordHash = await bcrypt.hash(password, 10);

  if (await client.user.findUnique({ where: { email } })) {
    res.status(400).json({ error: "email already taken" });
    return;
  }

  const user = await client.user.create({
    data: {
      firstName,
      lastName,
      email,
      passwordHash,
    },
  });

  const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET!!);

  const { passwordHash: _, ...userWithoutPassword } = user;

  res.status(201).json({ user: userWithoutPassword, token });
};

const getSchedules: Endpoint = (deps) => [
  async (req: RequestWithJWTBody, res) => {
    const { client } = deps;
    const userId = req.jwtBody?.userId;

    const user = await client.user.findUnique({ where: { id: userId } });
    if (!user) {
      res.status(404).json({ error: "resource not found" });
      return;
    }

    const schedules = await client.schedule.findMany({
      where: {
        userId,
      },
      include: {
        reptile: true,
      },
    });

    res.status(200).json({ schedules });
  },
];

export const usersController = controller("users", [
  { path: "/", method: "post", endpoint: createUser, skipAuth: true },
  { path: "/me", method: "get", endpoint: getMe },
  { path: "/me/schedules", method: "get", endpoint: getSchedules },
]);

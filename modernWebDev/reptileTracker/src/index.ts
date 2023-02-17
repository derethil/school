import express from "express";
import { PrismaClient } from "@prisma/client";

import dotenv from "dotenv";
import { ControllerDependencies } from "./dto/controllers";
import { usersController } from "./controllers/users";

dotenv.config();

const client = new PrismaClient();
const app = express();
app.use(express.json());

const controllerDependencies: ControllerDependencies = {
  client,
};

usersController(app, controllerDependencies);

app.listen(process.env.PORT || "3000", () => {
  console.log(`Started server on port ${process.env.PORT}`);
});

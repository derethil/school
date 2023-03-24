import express from "express";
import { PrismaClient } from "@prisma/client";
import cors from "cors";

import dotenv from "dotenv";
import { ControllerDependencies } from "./dto/controllers";
import { usersController } from "./controllers/users";
import { authController } from "./controllers/auth";
import { reptilesController } from "./controllers/reptiles";

dotenv.config();

const client = new PrismaClient();
const app = express();
app.use(express.json());
app.use(cors());

const controllerDependencies: ControllerDependencies = {
  client,
};

usersController(app, controllerDependencies);
authController(app, controllerDependencies);
reptilesController(app, controllerDependencies);

app.listen(process.env.PORT || "3000", () => {
  console.log(`Started server on port ${process.env.PORT}`);
});

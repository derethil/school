import { PrismaClient } from "@prisma/client";
import { RequestHandler } from "express";

export interface ControllerDependencies {
  client: PrismaClient;
}

export type Endpoint = (
  deps: ControllerDependencies
) => RequestHandler | RequestHandler[];

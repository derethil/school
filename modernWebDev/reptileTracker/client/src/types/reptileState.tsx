import { Reptile } from "@prisma/client";

export type ReptileState = Omit<
  Reptile,
  "id" | "createdAt" | "updatedAt" | "userId"
>;

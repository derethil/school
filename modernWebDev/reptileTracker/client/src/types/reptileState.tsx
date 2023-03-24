import { Reptile, Schedule } from "@prisma/client";

export type ReptileState = Omit<
  Reptile,
  "id" | "createdAt" | "updatedAt" | "userId"
>;

export interface ScheduleWithReptile extends Schedule {
  reptile: Reptile;
}

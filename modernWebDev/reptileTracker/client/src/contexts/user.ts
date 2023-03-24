import { createContext } from "react";
import { User as UserModel } from "@prisma/client";

type User = Omit<UserModel, "passwordHash">;

export const UserContext = createContext<User | null>(null);

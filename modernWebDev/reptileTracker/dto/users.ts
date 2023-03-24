import { User } from "@prisma/client";
import { ErrorResponse } from "./requests";

export interface CreateUserBody {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface SuccessfulAuthenticateResponse {
  user: Omit<User, "passwordHash">;
  token: string;
}

export type AuthenticateResponse =
  | SuccessfulAuthenticateResponse
  | ErrorResponse;

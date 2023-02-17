import { RequestHandler } from "express";
import jwt from "jsonwebtoken";
import { JWTBody, RequestWithJWTBody } from "../dto/jwt";

export const authenticationMiddleware: RequestHandler = async (
  req: RequestWithJWTBody,
  _,
  next
) => {
  const token = req.headers.authorization?.split(" ")[1];
  try {
    const jwtBody = jwt.verify(token || "", process.env.JWT_SECRET!!) as JWTBody;
    req.jwtBody = jwtBody;
  } catch (error) {
    console.log("token not valid");
  } finally {
    next();
  }
};

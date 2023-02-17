import { RequestHandler } from "express";
import jwt from "jsonwebtoken";
import { JWTBody, RequestWithJWTBody } from "../dto/jwt";

export const authenticationMiddleware: RequestHandler = async (
  req: RequestWithJWTBody,
  res,
  next
) => {
  const token = req.headers.authorization?.split(" ")[1];
  try {
    const jwtBody = jwt.verify(token || "", process.env.JWT_SECRET!!) as JWTBody;
    req.jwtBody = jwtBody;
    next();
  } catch (error) {
    res.status(401).json({ error: "unauthorized" });
  }
};

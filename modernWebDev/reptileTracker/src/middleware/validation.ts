import { RequestHandler } from "express";

export const validationMiddleware: RequestHandler = async (req, res, next) => {
  const id = parseInt(req.params.id);

  if (isNaN(id)) {
    res.status(400).json({ error: "invalid request" });
    return;
  }

  next();
};

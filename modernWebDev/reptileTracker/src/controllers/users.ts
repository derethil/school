import { RequestHandler } from "express";
import { Endpoint } from "../dto/controllers";
import { controller } from "../lib/controller";

const getRoot: Endpoint = (deps) => async (req, res) => {
  res.send(`<h1>Hello, world!</h1>`);
};

export const usersController = controller("users", [
  { path: "/", method: "get", endpoint: getRoot, skipAuth: true },
]);

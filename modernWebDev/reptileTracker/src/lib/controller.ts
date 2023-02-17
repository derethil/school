import { Express, Router } from "express";
import { RequestHandler } from "express";
import { ControllerDependencies, Endpoint } from "../dto/controllers";
import { authenticationMiddleware } from "../middleware/authentication";

interface Route {
  path: string;
  method: "get" | "post" | "put" | "delete";
  endpoint: Endpoint;
  skipAuth?: boolean;
}

export const controller =
  (name: string, routes: Route[]) => (app: Express, deps: ControllerDependencies) => {
    const router = Router({ mergeParams: true });

    routes.forEach((route) => {
      const { path, method, endpoint, skipAuth = false } = route;
      const routeMiddlewares: RequestHandler[] = [];
      if (!skipAuth) routeMiddlewares.push(authenticationMiddleware);
      router[method](path, routeMiddlewares, endpoint(deps));
    });

    app.use(`/${name}`, router);
  };

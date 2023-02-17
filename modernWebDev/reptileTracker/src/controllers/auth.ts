import bcrypt from "bcrypt";
import jwt from "jsonwebtoken";
import { Endpoint } from "../dto/controllers";
import { controller } from "../lib/controller";

interface LoginBody {
  email: string;
  password: string;
}

const Login: Endpoint = (deps) => async (req, res) => {
  const { client } = deps;
  const { email, password } = req.body as LoginBody;

  const user = await client.user.findUnique({ where: { email } });

  if (!user) {
    res.status(404).json({ error: "Invalid email or password" });
    return;
  }

  const isValidPassword = await bcrypt.compare(password, user.passwordHash);

  if (!isValidPassword) {
    res.status(401).json({ error: "Invalid email or password" });
    return;
  }

  const token = jwt.sign({ userId: user.id }, process.env.JWT_SECRET!!);

  const { passwordHash: _, ...userWithoutPassword } = user;

  res.json({ user: userWithoutPassword, token });
};

export const authController = controller("auth", [
  { path: "/login", method: "post", endpoint: Login, skipAuth: true },
]);

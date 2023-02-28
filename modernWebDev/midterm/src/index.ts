import express from "express";
import { PrismaClient } from "@prisma/client";
import dotenv from "dotenv";
import cors from "cors";
dotenv.config();

const client = new PrismaClient();
const app = express();
app.use(express.json());
app.use(cors()); // need for cross origin requests

interface CreateTodoBody {
  title: string;
}

app.post("/todos", async (req, res) => {
  const { title } = req.body as CreateTodoBody;

  const todo = await client.todo.create({
    data: {
      title,
    },
  });

  res.json({ todo });
});

app.get("/todos", async (req, res) => {
  const todos = await client.todo.findMany();
  res.json({ todos });
});

interface UpdateTodoBody {
  isCompleted: boolean;
}

app.put("/todos/:id", async (req, res) => {
  const { id } = req.params;
  const { isCompleted } = req.body as UpdateTodoBody;

  const todo = await client.todo.update({
    where: {
      id: Number(id),
    },
    data: {
      isCompleted,
    },
  });

  res.json({ todo });
});

app.listen(process.env.PORT, () => {
  // log url to console
  console.log(`Listening on port ${process.env.PORT}`);
});

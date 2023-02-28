import express from "express";
import { PrismaClient } from "@prisma/client";
import dotenv from "dotenv";
import cors from "cors";
dotenv.config();

const client = new PrismaClient();
const app = express();
app.use(express.json());
app.use(cors()); // need for cross origin requests


app.post("/todos", async (req, res) => {
  // todo implement this endpoint
  res.json({ todo: {} })
})

app.get("/todos", async (req, res) => {
  // TODO implement this endpoint!
  res.json({ todos: [] });
})

app.post("/todos/:id", async (req, res) => {
  // TODO implement this endpoint
  res.json({ todo: {} })
})

app.listen(process.env.PORT, () => {
  console.log(`Listening on port ${process.env.PORT}`);
});
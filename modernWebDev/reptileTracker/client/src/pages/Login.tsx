import { useState } from "react";
import { Button } from "react-daisyui";
import { Link } from "react-router-dom";

import { Input } from "../components/Input";
import { PageWrapper } from "../components/PageWrapper";

export function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Login</h1>

      <form className="mt-4">
        <Input
          label="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="mt-4"
        />

        <Input
          label="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
          className="mt-4"
        />

        <div className="flex justify-between items-center mt-4">
          <div className="flex text-sm text-base-300">
            <span className="mr-1">Don't have an account?</span>
            <span>
              <Link to="/register">
                <p className="link link-neutral">Register</p>
              </Link>
            </span>
          </div>

          <Button type="button" color="primary">
            Login
          </Button>
        </div>
      </form>
    </PageWrapper>
  );
}

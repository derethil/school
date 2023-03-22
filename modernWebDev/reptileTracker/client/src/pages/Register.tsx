import { useState } from "react";
import { Button, Link } from "react-daisyui";
import { Link as RouterLink } from "react-router-dom";
import { PageWrapper } from "../components/PageWrapper";
import { Input } from "../components/Input";

export function Register() {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Register</h1>

      <form className="mt-4">
        <Input
          label="First Name"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />

        <Input
          label="Last Name"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
          className="mt-4"
        />

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
            <span className="mr-1">Already have an account?</span>
            <span>
              <RouterLink to="/login">
                <p className="link link-neutral">Login</p>
              </RouterLink>
            </span>
          </div>

          <Button type="button" color="primary">
            Register
          </Button>
        </div>
      </form>
    </PageWrapper>
  );
}

import { useContext, useEffect, useState } from "react";
import { Alert, Button } from "react-daisyui";
import { Link, useNavigate } from "react-router-dom";

import { AuthenticateResponse, CreateUserBody } from "../../../dto/users";

import { PageWrapper } from "../components/PageWrapper";
import { Input } from "../components/Input";
import { useApi } from "../hooks/useApi";
import { AuthContext } from "../contexts/auth";
import { sentenceCase } from "../util/stringCases";
import { useUser } from "../hooks/useUser";
import { AlertState } from "../types/alert";

export function Register() {
  const api = useApi();
  const { setUser } = useUser();
  const { token, setToken } = useContext(AuthContext);
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [alert, setAlert] = useState<AlertState | null>(null);

  const handleRegister = async () => {
    if (!firstName || !lastName || !email || !password) {
      setAlert({ status: "warning", message: "Please fill out all fields" });
      return;
    }

    const body: CreateUserBody = {
      firstName,
      lastName,
      email,
      password,
    };

    const resultBody: AuthenticateResponse = await api.post("/users", body);

    if ("token" in resultBody) {
      setToken(resultBody.token);
      setUser(resultBody.user);
      setTimeout(() => {
        navigate("/dashboard", {
          replace: true,
        });
      }, 0);
    } else {
      setAlert({ status: "warning", message: sentenceCase(resultBody.error) });
    }
  };

  useEffect(() => {
    if (window.localStorage.getItem("token")) {
      navigate("/", {
        replace: true,
      });
    }
  }, [token]);

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Register</h1>

      <form className="mt-4" onSubmit={handleRegister}>
        {alert && (
          <Alert status={alert.status} className="mb-4">
            {alert.message}
          </Alert>
        )}

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
              <Link to="/login">
                <p className="link link-neutral">Login</p>
              </Link>
            </span>
          </div>

          <Button type="button" color="primary" onClick={handleRegister}>
            Register
          </Button>
        </div>
      </form>
    </PageWrapper>
  );
}

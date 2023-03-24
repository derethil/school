import { useContext, useEffect, useState } from "react";
import { Alert, Button } from "react-daisyui";
import { Link, useNavigate } from "react-router-dom";

import { Input } from "../components/Input";
import { PageWrapper } from "../components/PageWrapper";
import { useApi } from "../hooks/useApi";

import { LoginBody } from "../../../dto/auth";
import { AuthContext } from "../contexts/auth";
import { sentenceCase } from "../util/stringCases";
import { UserContext } from "../contexts/user";
import { AuthenticateResponse } from "../../../dto/users";
import { useUser } from "../hooks/useUser";
import { AlertState } from "../types/alert";

export function Login() {
  const api = useApi();
  const { token, setToken } = useContext(AuthContext);
  const { setUser } = useUser();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [alert, setAlert] = useState<AlertState | null>(null);

  useEffect(() => {
    if (window.localStorage.getItem("token")) {
      navigate("/dashboard", {
        replace: true,
      });
    }
  }, [token]);

  const handleLogin = async () => {
    if (!email || !password) {
      setAlert({ status: "warning", message: "Please fill out all fields" });
      return;
    }

    const body: LoginBody = {
      email,
      password,
    };

    const resultBody: AuthenticateResponse = await api.post("/auth", body);

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

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Login</h1>

      {alert && (
        <Alert status={alert.status} className="mt-4">
          {alert.message}
        </Alert>
      )}

      <form className="" onSubmit={handleLogin}>
        <Input
          label="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="mt-4 w-96"
        />

        <Input
          label="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          type="password"
          className="mt-4 w-96"
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

          <Button type="button" color="primary" onClick={handleLogin}>
            Login
          </Button>
        </div>
      </form>
    </PageWrapper>
  );
}

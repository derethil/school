import { useContext, useEffect, useState } from "react";
import { Button } from "react-daisyui";
import { Link, useNavigate } from "react-router-dom";

import { Input } from "../components/Input";
import { PageWrapper } from "../components/PageWrapper";
import { useApi } from "../hooks/useApi";

import { LoginBody } from "../../../dto/auth";
import { AuthContext } from "../contexts/auth";
import { sentenceCase } from "../util/stringCases";

export function Login() {
  const api = useApi();
  const { token, setToken } = useContext(AuthContext);
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (token?.length > 0) {
      navigate("/dashboard", {
        replace: true,
      });
    }
    console.log(token);
  }, [token]);

  const handleLogin = async () => {
    if (!email || !password) {
      setError("Please fill out all fields");
      return;
    }

    const body: LoginBody = {
      email,
      password,
    };

    const resultBody = await api.post("/auth", body);

    if (resultBody.token) {
      setToken(resultBody.token);
      setTimeout(() => {
        navigate("/dashboard", {
          replace: true,
        });
      }, 0);
    } else {
      setError(sentenceCase(resultBody.error));
    }
  };

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Login</h1>

      {error && <p className="mb-4 text-error">{error}</p>}

      <form className="mt-4" onSubmit={handleLogin}>
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

          <Button type="button" color="primary" onClick={handleLogin}>
            Login
          </Button>
        </div>
      </form>
    </PageWrapper>
  );
}

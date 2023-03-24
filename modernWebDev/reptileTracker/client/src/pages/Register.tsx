import { useContext, useEffect, useState } from "react";
import { Button } from "react-daisyui";
import { Link, useNavigate } from "react-router-dom";

import { CreateUserBody } from "../../../dto/users";

import { PageWrapper } from "../components/PageWrapper";
import { Input } from "../components/Input";
import { useApi } from "../hooks/useApi";
import { AuthContext } from "../contexts/auth";
import { sentenceCase } from "../util/stringCases";

export function Register() {
  const api = useApi();
  const { token, setToken } = useContext(AuthContext);
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleRegister = async () => {
    if (!firstName || !lastName || !email || !password) {
      setError("Please fill out all fields");
      return;
    }

    const body: CreateUserBody = {
      firstName,
      lastName,
      email,
      password,
    };

    const resultBody = await api.post("/users", body);

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

  useEffect(() => {
    if (token?.length > 0) {
      navigate("/dashboard", {
        replace: true,
      });
    }
  }, [token]);

  return (
    <PageWrapper center={false}>
      <h1 className="text-4xl font-bold">Register</h1>

      <form className="mt-4" onSubmit={handleRegister}>
        {error && <p className="mb-4 text-error">{error}</p>}

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

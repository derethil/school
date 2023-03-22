import { Button } from "react-daisyui";
import { Link } from "react-router-dom";
import { PageWrapper } from "../components/PageWrapper";

export function Home() {
  return (
    <PageWrapper>
      <h1 className="text-5xl font-bold">Reptile Tracker</h1>

      <p className="mt-4 text-xl">
        A simple app to track reptiles and their care.
      </p>

      <div className="mt-4">
        <Link to="/login">
          <Button color="primary" className="mr-4">
            Login
          </Button>
        </Link>
        <Link to="/register">
          <Button color="primary">Register</Button>
        </Link>
      </div>
    </PageWrapper>
  );
}

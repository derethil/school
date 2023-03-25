import { useContext } from "react";
import { Button } from "react-daisyui";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../contexts/auth";
import { useUser } from "../hooks/useUser";

interface HeaderProps {
  pageTitle: string;
}

export function Header(props: HeaderProps) {
  const { user } = useUser();

  const navigate = useNavigate();
  const { setToken } = useContext(AuthContext);

  const handleLogout = () => {
    setToken("");
    setTimeout(() => navigate("/"), 0);
  };

  return (
    <div className="flex justify-between items-start mb-4">
      <h1 className="text-4xl mb-6">{props.pageTitle}</h1>
      <div className="flex items-center">
        <p className="text-sm mr-2">
          {user?.firstName} {user?.lastName}
        </p>
        <Button className="btn-primary" onClick={handleLogout}>
          Logout
        </Button>
      </div>
    </div>
  );
}

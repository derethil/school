import { useContext } from "react";
import { UserContext } from "../contexts/user";

const useUser = () => {
  const userContext = useContext(UserContext);
};

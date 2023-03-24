import { AlertProps } from "react-daisyui";

export interface AlertState {
  status: AlertProps["status"];
  message: string;
}

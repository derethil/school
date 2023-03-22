import { Input as DaisyInput } from "react-daisyui";

interface TextInputProps {
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  label: string;
  type?: React.HTMLInputTypeAttribute;
  className?: string;
}

export function Input(props: TextInputProps) {
  const defaultClassName =
    "flex flex-col text-sm font-medium text-accent-content ";

  return (
    <label className={defaultClassName + props.className}>
      <p className="text-lg">{props.label}</p>
      <DaisyInput
        className="mt-1 w-96"
        type={props.type ?? "text"}
        placeholder={props.label}
        value={props.value}
        onChange={props.onChange}
      />
    </label>
  );
}

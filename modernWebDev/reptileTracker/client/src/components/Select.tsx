import {
  Select as DaisySelect,
  SelectProps as DaisySelectProps,
} from "react-daisyui";

interface DropdownProps extends DaisySelectProps {
  label: string;
  className?: string;
}

export function Select(props: DropdownProps) {
  const defaultClassName =
    "flex flex-col text-sm font-medium text-accent-content ";

  return (
    <label className={defaultClassName + props.className}>
      <p className="text-lg">{props.label}</p>
      <DaisySelect className="mt-1 w-96" {...props}>
        {props.children}
      </DaisySelect>
    </label>
  );
}

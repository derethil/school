import {
  CheckboxProps as DaisyCheckboxProps,
  Checkbox as DaisyCheckbox,
} from "react-daisyui";

interface CheckboxProps extends DaisyCheckboxProps {
  label: string;
  labelClassName?: string;
}

export function Checkbox(props: CheckboxProps) {
  return (
    <label className={"flex items-center " + props.labelClassName}>
      <DaisyCheckbox className="md-0" {...props} />
      <span className="ml-2">{props.label}</span>
    </label>
  );
}

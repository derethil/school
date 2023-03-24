import { ReactNode } from "react";

interface PageWrapperProps {
  children?: ReactNode;
  center?: boolean;
}

export function PageWrapper({ children, center = true }: PageWrapperProps) {
  const centerClass = center ? "items-center" : "";
  return (
    <div className="flex flex-col items-center mt-8 text-neutral">
      <div className={`flex flex-col ${centerClass}`}>{children}</div>
    </div>
  );
}

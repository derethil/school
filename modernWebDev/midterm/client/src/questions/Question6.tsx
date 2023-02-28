import { useEffect, useState } from "react";

function useTimer() {
  const [enabled, setEnabled] = useState(false);
  const [count, setCount] = useState(0);

  useEffect(() => {
    if (!enabled) return;

    const interval = setInterval(() => {
      setCount((prev) => prev + 1);
    }, 1000);

    return () => clearInterval(interval);
  }, [enabled]);

  const toggleEnabled = () => setEnabled((prev) => !prev);

  return [count, toggleEnabled] as const;
}

export const Question6 = () => {
  const [count, toggleEnabled] = useTimer();

  return (
    <section className="question">
      <h2>Question 6</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          Build me an auto-counter application that meets the following requirements:
          <ol>
            <li>The count starts at 0</li>
            <li>Provide me with a checkbox that turns off/on the auto-counter</li>
            <li>While the auto-counter is on the count increments by 1 every second</li>
            <li>
              When I turn the auto-counter off then the counter should stop until I turn
              it back on
            </li>
          </ol>
          <div>Be sure to follow unidirectional dataflow!</div>
          Hint: you will need a useEffect hook.
        </div>
        <hr />
        <div className="solution-section">
          <div>{count}</div>
          <input type="checkbox" onChange={toggleEnabled} />
          <label>Auto-counter</label>
        </div>
      </div>
    </section>
  );
};

import { useState } from "react";

export const Question1 = () => {
  const [count, setCount] = useState(0);

  return (
    <section className="question">
      <h2>Question 1</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          In space below, build a basic counter application that does the following:
          <ol>
            <li>Allows me to increment the count by 1</li>
            <li>Allows me to decrement the count by 1</li>
            <li>Allows me to reset the count to 0</li>
            <li>The count should be displayed and updated as I press the buttons</li>
          </ol>
          Ensure that you are correctly following unidirectional dataflow.
        </div>
        <hr />
        <div className="solution-section">
          <div>{count}</div>
          <div>
            <button onClick={() => setCount(count + 1)}>Increment</button>
            <button onClick={() => setCount(count - 1)}>Decrement</button>
            <button onClick={() => setCount(0)}>Reset</button>
          </div>
        </div>
      </div>
    </section>
  );
};

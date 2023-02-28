export const Question7 = () => {
  return (
    <section className="question">
      <h2>Question 7</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          Explain to me in one paragraph in your own words what password hashing is, and
          why we do it? Give me an example of how you might implement it in TypeScript.
        </div>
        <hr />
        <div className="solution-section">
          <div>
            Password hashing is a one way function that takes a password, adds a salt, and
            converts it into a hash. The hash is then stored in the database. When a user
            attempts to log in, the password they enter is hashed and compared to the hash
            in the database. If the hashes match, the user is authenticated. If the hashes
            do not match, the user is not authenticated. This is much more secure than
            storing passwords in plain text as the hashes are not reversible. Any attacker
            who gains access to the database would not be able to retrieve the passwords,
            and would have to brute force the hashes.
          </div>

          <div>
            <pre>
              {`import bcrypt from 'bcrypt';

const hashedPassword = await bcrypt.hash(password, 10); // handles salting and hash

// Store hashedPassword in database`}
            </pre>
          </div>
        </div>
      </div>
    </section>
  );
};

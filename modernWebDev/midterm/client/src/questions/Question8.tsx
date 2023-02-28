export const Question8 = () => {
  return (
    <section className="question">
      <h2>Question 8</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          My colleague has told me that their software is perfect and has no bugs, and
          therefore it is unessasary for them to hash passwords because their database
          will never get leaked.
          <div>
            Assume, for the sake of argument, that the software is, in fact, perfect with
            no issues. Do you agree with my colleages assessment? Why or why not? Be sure
            to provide specific examples.
          </div>
        </div>
        <hr />
        <div className="solution-section">
          No. Even if the software is perfect and there are no technical security flaws, a
          large portion of hacking is social engineering. No amount of perfect software
          will stop someone with proper access just <i>giving</i> improper access to
          someone else. In addition, if passwords are stored in plain text, they can be
          easily read and used by people who have access to the database. Imagine if an
          employee could just log in as a user and see all of their personal data - this
          is obviously a huge security issue. Even if the database is not leaked, it is
          still important to hash passwords.
        </div>
      </div>
    </section>
  );
};

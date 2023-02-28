import { useState } from "react";

let nextId = 0; // bad but this is just a simple question

interface Note {
  id: number;
  text: string;
}

export const Question2 = () => {
  const [inProgressNote, setInProgressNote] = useState("");
  const [noteList, setNoteList] = useState<Note[]>([]);

  const saveNote = () => {
    setNoteList([...noteList, { id: nextId++, text: inProgressNote }]);
    setInProgressNote("");
  };

  const deleteNote = (idToDelete: number) => {
    setNoteList(noteList.filter((note) => note.id !== idToDelete));
  };

  return (
    <section className="question">
      <h2>Question 2</h2>
      <div className="question-content">
        <strong>Instructions</strong>
        <div className="question-instructions">
          You are going to build me a simple note taking application that does the
          following
          <ol>
            <li>Gives me an input where I can type my note</li>
            <li>Gives me a save button</li>
            <li>
              When the save button is pressed the following should happen:
              <ol type="a">
                <li>The text I typed into the input field gets cleared out.</li>
                <li>The note I typed gets displayed in a list of notes on the screen</li>
              </ol>
            </li>
            <li>
              On each note, there should be a button that allows me to delete the note
              from the list.
            </li>
          </ol>
          Ensure that you are correctly following unidirectional dataflow.
        </div>
        <hr />
        <div className="solution-section">
          <div>
            <input
              type="text"
              value={inProgressNote}
              onChange={(e) => setInProgressNote(e.target.value)}
            />
            <button onClick={() => saveNote()}>Save Note</button>
          </div>
          <div>
            {noteList.map((note) => {
              return (
                <div
                  key={note.id}
                  style={{
                    width: "17.3em",
                    display: "flex",
                    justifyContent: "space-between",
                  }}
                >
                  <span>{note.text}</span>
                  <button onClick={() => deleteNote(note.id)}>Delete</button>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </section>
  );
};

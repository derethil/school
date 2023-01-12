import { FormEvent, useState } from "react";
import "../css/App.css";
import { QuoteCard } from "./QuoteCard";
import { Quote } from "../types";

function App() {
  const [error, setError] = useState("");
  const [quotes, setQuotes] = useState<Quote[]>([]);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const input = new FormData(event.target as HTMLFormElement).get("search");

    setError("");
    setQuotes([]);

    if (input) {
      const result = await fetch(
        `https://api.quotable.io/search/quotes?query=${input}&fields=author`
      );
      const data = await result.json();

      if (data.results.length === 0) {
        setError("No quotes found");
      } else {
        setQuotes(
          data.results.map((quote: any) => ({
            content: quote.content,
            author: quote.author,
          }))
        );
      }
    } else {
      setError("No search term entered");
    }
  };

  console.log(quotes);

  return (
    <main className="wrapper">
      <form id="form" onSubmit={handleSubmit}>
        <label htmlFor="search">
          <h1>Quote Search</h1>
        </label>

        <div id="search-container">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" />
          <input
            type="text"
            id="search"
            name="search"
            placeholder="e.g. Thomas Jefferson"
          />
          <p id="search-error">{error}</p>
        </div>
      </form>

      <section id="quotes">
        {quotes.map((quote, index) => (
          <QuoteCard quote={quote} key={index} />
        ))}
      </section>
    </main>
  );
}

export default App;

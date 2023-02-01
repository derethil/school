import { FormEvent, useEffect, useState } from "react";
import "../css/App.css";
import { QuoteCard } from "./QuoteCard";
import { Quote } from "../types";

function App() {
  const [error, setError] = useState("");
  const [quotes, setQuotes] = useState<Quote[]>([]);

  const handleSubmit = async (event: FormEvent) => {
    event.preventDefault();
    const input = new FormData(event.target as HTMLFormElement).get("search");

    if (input) {
      const result = await fetch(
        `https://api.quotable.io/search/quotes?query=${input}&fields=author`
      );
      const data = await result.json();

      if (data.results.length === 0) {
        setError("No quotes found");
      } else {
        setError("");
        setQuotes(
          data.results.map((quote: any) => ({
            id: quote._id,
            content: quote.content,
            author: quote.author,
          }))
        );
      }
    } else {
      setError("No search term entered");
    }
  };

  const fetchRandomQuote = async () => {
    const result = await fetch("https://api.quotable.io/random");
    const data = await result.json();

    setQuotes([
      {
        id: data._id,
        content: data.content,
        author: data.author,
        isRandom: true,
      },
    ]);
  };

  useEffect(() => {
    fetchRandomQuote();
  }, []);

  return (
    <main
      id="wrapper"
      className={quotes.length === 0 || quotes[0]?.isRandom ? "centered" : "top"}
    >
      <label htmlFor="search">
        <h1>Quote Search</h1>
      </label>

      <form id="form" onSubmit={handleSubmit}>
        <section id="search-container">
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" />
          <input
            type="text"
            id="search"
            name="search"
            placeholder="e.g. Thomas Jefferson"
          />
          <p id="search-error">{error}</p>
        </section>
      </form>

      <section id="quotes" className={quotes[0]?.isRandom ? "random" : "searched"}>
        {quotes.map((quote) => (
          <QuoteCard quote={quote} key={quote.id} />
        ))}
      </section>
    </main>
  );
}

export default App;

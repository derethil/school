import { Quote } from "../types";

import "../css/QuoteCard.css";

interface QuoteCardProps {
  quote: Quote;
}

export function QuoteCard(props: QuoteCardProps) {
  return (
    <div className="quote-card">
      <p className="quote-content">{props.quote.content}</p>
      <p className="quote-author">- {props.quote.author}</p>
    </div>
  );
}

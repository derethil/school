export function snakeToSentence(str: string) {
  return str
    .split("_")
    .map((word) => word[0].toUpperCase() + word.slice(1))
    .join(" ");
}

// Capitalize the first letter of a string
export function sentenceCase(str: string) {
  return str[0].toUpperCase() + str.slice(1);
}

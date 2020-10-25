import java.util.Objects;

import static java.lang.String.format;

public class WordFreq implements Comparable<WordFreq> {
    private int frequency = 0;
    String word;

    public WordFreq(String word) {
        this.word = word;
    }

    public void incrementFrequency() { frequency ++; }

    public String toString() {
        return format(":%s: %s", word, frequency);
    }

    @Override
    public int compareTo(WordFreq wordFrequency) {
        return word.compareTo(wordFrequency.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordFreq wordFreq = (WordFreq) o;
        return Objects.equals(word, wordFreq.word);
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }
}

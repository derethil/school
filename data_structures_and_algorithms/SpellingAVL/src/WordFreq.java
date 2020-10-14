import java.util.Objects;

import static java.lang.String.format;

public class WordFreq implements Comparable<WordFreq> {
    int frequency = 0;
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
    public boolean equals(Object obj) {
        return word.equals(obj);
    }

}

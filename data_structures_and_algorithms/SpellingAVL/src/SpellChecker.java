import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class SpellChecker {


    private static int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();

        //System.out.print( "minDistance " + word1 + " " + word2 + ": ");
        // len1+1, len2+1, because finally return dp[len1][len2]
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }

        //iterate though, and check last char
        for (int i = 0; i < len1; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < len2; j++) {
                char c2 = word2.charAt(j);

                //if last two chars equal
                if (c1 == c2) {
                    //update dp value for +1 length
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int delete= dp[i][j + 1] + 1;
                    int insert = dp[i + 1][j] + 1;

                    int min = Math.min(replace, insert);
                    min = Math.min(delete, min);
                    dp[i + 1][j + 1] = min;
                }
            }
        }
        //System.out.println(dp[len1][len2]);
        return dp[len1][len2];
    }

    private static void checkWord(String wordToCheck, Dictionary dict, AVLTree<WordFreq> misspelled) {
        if (dict.avl.contains(wordToCheck) != null) { return; }
        if (wordToCheck.equals("")) { return; }

        WordFreq currentFreq = new WordFreq(wordToCheck);
        if (misspelled.contains(currentFreq) == null) {
            currentFreq.incrementFrequency();
            misspelled.insert(currentFreq);
        } else {
            misspelled.contains(currentFreq).incrementFrequency();
            return;
        }

        System.out.printf("\nfound misspelled :%s:\n", wordToCheck);

        int closeCount = 0;

        for (String dictWord: dict.lst) {
            int distance = minDistance(wordToCheck, dictWord);
            if (distance <= 2 && closeCount <= 10) {
                closeCount++;
                System.out.printf("%s(%s) ", dictWord, distance);
            } else if (distance < 2) {
                closeCount++;
                System.out.printf("%s(%s) ", dictWord, distance);
            }
        }
        if (closeCount == 0) { System.out.printf("No close words found"); }
        System.out.println();

    }

    private static void checkFile(String filename) {
        Dictionary dict = new Dictionary();
        AVLTree<WordFreq> misspelled = new AVLTree<>();

        try {
            Scanner reader = new Scanner(new File("resources/" + filename));

            System.out.println("\nFile " + filename);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                line = line.toLowerCase().replaceAll("\\p{Punct}", "");
                String[] words = line.split(" ");
                for (String word: words) {
                    checkWord(word, dict, misspelled);
                }
            }
            System.out.println();
            misspelled.printTree("Misspelled words of " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static class Dictionary {
        ArrayList<String> lst = new ArrayList<>();
        AVLTree<String> avl = new AVLTree<>();
        
        public Dictionary() {
            try {
                Scanner reader = new Scanner(new File("resources/dictionary.txt"));

                while (reader.hasNextLine()) {
                    String line = reader.nextLine();
                    lst.add(line);
                    avl.insert(line);
                }
                reader.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    


    public static void main(String[] args) {
        checkFile("paragraph1.txt");
        checkFile("paragraph2.txt");
        checkFile("paragraph3.txt");
    }
}

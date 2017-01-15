package com.yogeshn.urbandictionaryscraper;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class SplitFiles {
    private static final Character FIRST_FIRST_LETTER_TO_SCRAPE = 'A';
    private static final Character LAST_FIRST_LETTER_TO_SCRAPE = 'Z';

    //1. Loop through the A-Z.txt files
    //2. For each file split into 30,000 lines and save with filename LETTER_NUMBER.txt
    //3. Hope that 30,000 is enough for Alexa Custom Slots

    public static void splitFiles() throws IOException {
        List<String> words = new LinkedList<>();
        int lineNumber = 0;
        int fileNumber = 1;
        for (char firstLetterOfDictionaryWord = FIRST_FIRST_LETTER_TO_SCRAPE; firstLetterOfDictionaryWord <= LAST_FIRST_LETTER_TO_SCRAPE; firstLetterOfDictionaryWord++) {
            try (BufferedReader br = new BufferedReader(new FileReader(firstLetterOfDictionaryWord + ".txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (lineNumber < 30000) {
                        words.add(line);
                        lineNumber++;
                    } else {
                        saveSplitFile(words, Character.toString(firstLetterOfDictionaryWord), fileNumber);
                        words.clear();
                        lineNumber = 0;
                        fileNumber++;
                    }
                }

                if (!words.isEmpty()) {
                    saveSplitFile(words, Character.toString(firstLetterOfDictionaryWord), fileNumber);
                }
            }
        }
    }

    private static void saveSplitFile(List<String> words, String letter, int fileNumber) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(letter + "_" + fileNumber + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String word : words) {
            writer.println(word);
        }

        writer.close();
    }

}

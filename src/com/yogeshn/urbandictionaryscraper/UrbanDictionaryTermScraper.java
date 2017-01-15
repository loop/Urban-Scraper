package com.yogeshn.urbandictionaryscraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UrbanDictionaryTermScraper {

    private static final String URBAN_DICTIONARY_BROWSE_URL_BASE = "http://www.urbandictionary.com/browse.php";
    private static final String URBAN_DICTIONARY_BROWSE_URL_CHARACTER_QUERY_PARAMETER = "character";
    private static final String URBAN_DICTIONARY_BROWSE_URL_PAGE_QUERY_PARAMETER = "page";
    private static final int MAX_PAGES_PER_FIRST_LETTER_TO_SCRAPE = 2000;
    private static final Character FIRST_FIRST_LETTER_TO_SCRAPE = 'A';
    private static final Character LAST_FIRST_LETTER_TO_SCRAPE = 'Z';

    public static void main(final String args[]) {
        try {
            getUrbanDictionaryWordsCompleteListFromInternet();
            SplitFiles.splitFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getUrbanDictionaryWordsCompleteListFromInternet() throws IOException {
        List<String> allWords = new LinkedList<>();
        for (char firstLetterOfDictionaryWord = FIRST_FIRST_LETTER_TO_SCRAPE; firstLetterOfDictionaryWord <= LAST_FIRST_LETTER_TO_SCRAPE; firstLetterOfDictionaryWord++) {
            for (int pageNumberToScrape = 1; pageNumberToScrape <= MAX_PAGES_PER_FIRST_LETTER_TO_SCRAPE; pageNumberToScrape++) {
                List<String> urbanDictionaryWordsPartialList = getUrbanDictionaryWordsPartialListFromInternet(firstLetterOfDictionaryWord, pageNumberToScrape);
                if (!urbanDictionaryWordsPartialList.isEmpty()) {
                    allWords.addAll(urbanDictionaryWordsPartialList);
                } else {
                    break;
                }
            }
            saveWords(allWords, firstLetterOfDictionaryWord);
            allWords.clear();
        }
    }

    private static void saveWords(List<String> urbanDictionaryWordsPartialList, char letter) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(letter + ".txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String word : urbanDictionaryWordsPartialList) {
            writer.println(word);
        }

        writer.close();
    }

    private static List<String> getUrbanDictionaryWordsPartialListFromInternet(final char firstLetterOfDictionaryWord, final int pageNumberToScrape) throws IndexOutOfBoundsException, MalformedURLException, IOException {
        List<String> urbanDictionaryWordsPartialList = new LinkedList<>();
        URL urbanDictionaryBrowseURL = getUrbanDictionaryBrowseURL(firstLetterOfDictionaryWord, pageNumberToScrape);

        Document doc = Jsoup.connect(urbanDictionaryBrowseURL.toString()).get();
        Elements newsHeadlines = doc.select("#columnist");

        if (!newsHeadlines.isEmpty()) {
            Elements elements = newsHeadlines.get(0).getElementsByTag("a");
            urbanDictionaryWordsPartialList.addAll(elements.stream().map(Element::text).collect(Collectors.toList()));
        }

        return urbanDictionaryWordsPartialList;
    }

    private static URL getUrbanDictionaryBrowseURL(final char firstLetterOfDictionaryWord, final int dictionaryPageNumber) throws MalformedURLException {
        if (firstLetterOfDictionaryWord < 'A'
                || firstLetterOfDictionaryWord > 'Z') {
            throw new MalformedURLException("Cannot get Urban Dictionary Browse URL for first letter: " + firstLetterOfDictionaryWord);
        } else if (dictionaryPageNumber < 0) {
            throw new MalformedURLException("Cannot get Urban Dictionary Browse URL for page number: " + dictionaryPageNumber);
        }
        URL urbanDictionaryBrowseURL;

        urbanDictionaryBrowseURL = new URL(URBAN_DICTIONARY_BROWSE_URL_BASE
                + "?" + URBAN_DICTIONARY_BROWSE_URL_CHARACTER_QUERY_PARAMETER + "=" + firstLetterOfDictionaryWord
                + "&" + URBAN_DICTIONARY_BROWSE_URL_PAGE_QUERY_PARAMETER + "=" + dictionaryPageNumber);

        return urbanDictionaryBrowseURL;
    }
}
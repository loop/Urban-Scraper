# Urban Scraper

This Java application scrapes UrbanDictionary pages and creates a `.txt` of all the available words on the site.

This was made to compliment the [UrbanMind](https://github.com/loop/UrbanMind) Alexa Skill application to extend the custom slot types terms.

This code currently uses [jsoup](http://jsoup.org) to extract the web page.

## Setup

Import this into your favorite IDE and add jsoup as a external library, then simply run `UrbanDictionaryTermScraper.java` and the program will start fetching the words.

Note: It takes approximately 15 minutes of a good internet connection to get all the words.
package com.english.englishwords.app.pojo;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by bogdank on 4/6/14.
 */
public class WordSense implements Serializable {
    private String word;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getConcatenatedExamples() {
        String concatenatedExamples = "";
        for (String example : examples) {
            concatenatedExamples+=example;
            concatenatedExamples+="\n";
        }
        return concatenatedExamples;
    }

    public String getConcatenatedSynonyms() {
        String concatenatedExamples = "";
        for (WordSense synonym : synonyms) {
            concatenatedExamples+=synonym.getWord();
            if (!synonym.getWord().equals(synonyms[synonyms.length-1].getWord())) {
                concatenatedExamples += ", ";
            }
        }
        return concatenatedExamples;
    }

    public void setExamples(String[] examples) {
        this.examples = examples;
    }

    public WordSense[] getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(WordSense[] synonyms) {
        this.synonyms = synonyms;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    private String definition;
    private String[] examples;
    private WordSense[] synonyms;
    private int rank;

    // for first 5k we may use translations instead of definitions
    // the structure may be updated
    // HashMap<LangCode, Translation>
    private HashMap<String, String> translations;
    private HashMap<String, String>[] exampleTranslations;
}

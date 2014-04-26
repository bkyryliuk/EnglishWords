package com.english.englishwords.app.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by bogdank on 4/6/14.
 */
public class WordValue implements Serializable {
    private String word;
    private int rank;
    private double priority;
    private ArrayList<WordSense> senses;

    // for first 5k we may use translations instead of definitions
    // the structure may be updated
    // HashMap<LangCode, Translation>
    private HashMap<String, String> translations;
    private HashMap<String, String>[] exampleTranslations;

    public WordValue(String word, int rank, ArrayList<WordSense> senses) {
        this.word = word;
        this.rank = rank;
        this.senses = new ArrayList<WordSense>(senses);
    }

    public String getWord() {
        return word;
    }


    public int getRank() {
        return rank;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public ArrayList<WordSense> getSenses() {
        return senses;
    }
}

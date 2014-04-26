package com.english.englishwords.app.pojo;

import java.util.ArrayList;

/**
 * Created by bogdank on 4/26/14.
 */
public class WordQueue {
    // limited list of words that are currently in the learning progress
    private ArrayList<WordValue> wordsInProgress;
    // words that were learned by the student
    private ArrayList<String> learnedWords;
    // words that have to be leared by the student
    private ArrayList<String> remainingWords;

    // init the word queue
    public WordQueue(ArrayList<WordValue> wordsInProgress, ArrayList<String> learnedWords,
                     ArrayList<String> remainingWords) {
        this.wordsInProgress = new ArrayList<WordValue>(wordsInProgress);
        this.learnedWords = new ArrayList<String>(learnedWords);
        this.remainingWords = new ArrayList<String>(remainingWords);
    }

    public void learnWord() {
        // TODO add logic of moving the word from the wordsInProgress to the learnedWords
        // and adding new word from remainingWords to the wordsInProgress
    }

    /**
     *
     * @return the WordValue with the highest priority value
     */
    public WordValue getWordToLearn() {
        // TODO
        return null;
    }

    public void updateWordPriority(WordValue wordValue, double priority) {
        // TODO
    }

    public void recalculateWordPriorities() {

    }


}

package com.english.englishwords.app.pojo;

import java.util.Random;

/**
 * Created by bogdank on 4/6/14.
 */
public class Exercise {
    public WordValue getWordSense() {
        return word;
    }

    public void setWord(WordValue word) {
        this.word = word;
    }

    public WordValue[] getRelatedWordValues() {
        return relatedWordValues;
    }

    public void setRelatedWordValues(WordValue[] relatedWordValues) {
        this.relatedWordValues = relatedWordValues;
        // update their definitions
        relatedWordSensesDefinitions = new String[relatedWordValues.length];
        for (int i = 0; i < relatedWordValues.length; ++i) {
            relatedWordSensesDefinitions[i] = relatedWordValues[i]
                    .getSenses().get(0).getDefinition();
        }
    }

    public String[] getRelatedWordSensesDefinitions() {
        return relatedWordSensesDefinitions;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRandomRightAnswer() {
        Random random = new Random();
        rightAnswer = random.nextInt(relatedWordValues.length);
        relatedWordValues[rightAnswer] = word;
        relatedWordSensesDefinitions[rightAnswer] = word.getSenses().get(0).getDefinition();

    }

    private WordValue word;
    private String[] relatedWordSensesDefinitions;
    private WordValue[] relatedWordValues;
    private int rightAnswer;

}

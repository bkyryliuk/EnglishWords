package com.english.englishwords.app.pojo;

import java.util.Random;

/**
 * Created by bogdank on 4/6/14.
 */
public class Exercise {
    public WordSense getWordSense() {
        return word;
    }

    public void setWord(WordSense word) {
        this.word = word;
    }

    public WordSense[] getRelatedWordSenses() {
        return relatedWordSenses;
    }

    public void setRelatedWordSenses(WordSense[] relatedWordSenses) {
        this.relatedWordSenses = relatedWordSenses;
        // update their definitions
        relatedWordSensesDefinitions = new String[relatedWordSenses.length];
        for (int i = 0; i < relatedWordSenses.length; ++i) {
            relatedWordSensesDefinitions[i] = relatedWordSenses[i].getDefinition();
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
        rightAnswer = random.nextInt(relatedWordSenses.length);
        relatedWordSenses[rightAnswer] = word;
        relatedWordSensesDefinitions[rightAnswer] = word.getDefinition();

    }

    private WordSense word;
    private String[] relatedWordSensesDefinitions;
    private WordSense[] relatedWordSenses;
    private int rightAnswer;

}

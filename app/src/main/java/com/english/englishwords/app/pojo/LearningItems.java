package com.english.englishwords.app.pojo;

import java.util.ArrayList;
import java.util.Arrays;

interface LearningItem extends PriorityItem {
    boolean isLearned();
}

public class LearningItems {
    private PriorityQueue wordsInProgress;
    private ArrayList<String> learnedElements;
    private ArrayList<String> remainingElements;

    LearningItems(ArrayList<LearningItem> elementsInProgress,
                  ArrayList<String> learnedElements, ArrayList<String> remainingElements) {
        this.wordsInProgress = new PriorityQueue(new ArrayList<PriorityItem>(
                Arrays.asList((LearningItem [])elementsInProgress.toArray())));
        this.learnedElements = learnedElements;
        this.remainingElements = remainingElements;
    }

    LearningItem getElement() {
        // TODO implement
        return null;
    }

    void SubmitElement(LearningItem item, ExerciseResults results) {
        // TODO implement
    }


}

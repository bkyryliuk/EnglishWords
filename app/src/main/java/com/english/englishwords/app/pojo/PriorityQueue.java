package com.english.englishwords.app.pojo;

import java.util.ArrayList;

interface PriorityItem {
  double getPriority();
  void setPriority(double priority);
}

public class PriorityQueue {
    // limited list of words that are currently in the learning progress
    private ArrayList<PriorityItem> elementsInProgress;

    // init the word queue
    public PriorityQueue(ArrayList<PriorityItem> elementsInProgress) {
        this.elementsInProgress = new ArrayList<PriorityItem>(elementsInProgress);
    }


    public void remove(PriorityItem element) {
        // TODO add logic of moving the word from the wordsInProgress to the learnedWords
        // and adding new word from remainingWords to the wordsInProgress
        elementsInProgress.remove(elementsInProgress.indexOf(elementsInProgress));
    }

    /**
     *
     * @return the WordValue with the highest priority value
     */
    public PriorityItem getTop() {
        PriorityItem maxElement = elementsInProgress.get(0);
        for (PriorityItem element : elementsInProgress) {
            if (element.getPriority() > maxElement.getPriority()) {
                maxElement = element;
            }
        }

        if(maxElement.getPriority() <= 0) {
            // TODO add new element to the elements in progress
        }

        return maxElement;
    }

    public void updatePriority(PriorityItem wordValue, double priority) {
        elementsInProgress.get(elementsInProgress.indexOf(wordValue)).setPriority(priority);
    }

    /**
     * Recalculate priorities based on the current timestamp during the application start
     */
    public void recalculatePriorities() {
        //TODO
    }


}

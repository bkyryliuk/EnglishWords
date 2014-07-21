package com.english.englishwords.app.excerciseproviders;

import com.english.englishwords.app.algo.DistanceWordSelector;
import com.english.englishwords.app.algo.LevenshteinDistanceCalculator;
import com.english.englishwords.app.dao.WordDAO;
import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.Word;
import com.english.englishwords.app.pojo.WordQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bogdank on 4/6/14.
 */
public class DefinitionExerciseProvider implements ExerciseProvider {
    private static final int DEFINITION_EXERCISE_OPTIONS_NUM = 6;

    private final WordDAO wordDao;

    public DefinitionExerciseProvider(WordDAO wordDao) {
        this.wordDao = wordDao;
    }

    public Exercise generateExerciseForWord(String wordToLearn) {
        // TODO(Bogdan) make some sort of mixed selector to be used here (pick related words based on multiple factors)
        DistanceWordSelector distanceWordSelector = new DistanceWordSelector(
                new LevenshteinDistanceCalculator());
        // TODO(Bogdan) pass here actual word instead of empty one just with the name
        List<Word> relatedWords = distanceWordSelector.SelectNClosestWords(
                wordDao.getWord(wordToLearn), wordDao.getAllWords(), DEFINITION_EXERCISE_OPTIONS_NUM
        );

        Exercise exercise = new Exercise();
        exercise.setLearningWord(wordDao.getWord(wordToLearn));
        exercise.setQuestion(exercise.getLearningWord().getWord());
        exercise.setOptionWords(GenerateOptions(exercise.getLearningWord(), relatedWords));

        List<String> options = new ArrayList<String>();
        for (Word option : exercise.getOptionWords()) {
            // TODO(krasikov): use other than first sense.
            // TODO(Bogdan): fail here
            if (option.getSenses().size() == 0) {
                options.add("Definition not found: " + option.getWord());
            } else {
                options.add(option.getSenses().get(0).getDefinition());
            }
        }
        exercise.setOptions(options.toArray(new String[0]));

        // Originally correct answer is at position 0.
        moveCorrectOptionToRandomPosition(exercise);

        return exercise;
    }

    void moveCorrectOptionToRandomPosition(Exercise exercise) {
        Random random = new Random();
        exercise.setCorrectOption(random.nextInt(exercise.getOptionWords().length));

        Word tmpWord = exercise.getOptionWords()[exercise.getCorrectOption()];
        exercise.getOptionWords()[exercise.getCorrectOption()] = exercise.getOptionWords()[0];
        exercise.getOptionWords()[0] = tmpWord;

        String tmpOption = exercise.getOptions()[exercise.getCorrectOption()];
        exercise.getOptions()[exercise.getCorrectOption()] = exercise.getOptions()[0];
        exercise.getOptions()[0] = tmpOption;
    }

    private Word[] GenerateOptions(Word word, List<Word> relatedWords) {
        Word[] optionWords = new Word[DEFINITION_EXERCISE_OPTIONS_NUM];
        // Later correct option will be moved to a random position.
        optionWords[0] = word;
        for (int i = 1; i < DEFINITION_EXERCISE_OPTIONS_NUM; ++i) {
            optionWords[i] = relatedWords.get(i);
        }
        return optionWords;
    }

    private String GetOptionCandidateWord() {
        // TODO(krasikov): user something smarter.
        return WordQueue.getInstance().getWordsInProgress().get(new Random().nextInt(100));
    }
}

package com.english.englishwords.app.excerciseproviders;

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
        Exercise exercise = new Exercise();
        exercise.setLearningWord(wordDao.getWord(wordToLearn));
        exercise.setQuestion(exercise.getLearningWord().getWord());
        exercise.setOptionWords(GenerateOptions(exercise.getLearningWord()));

        List<String> options = new ArrayList<String>();
        for (Word option : exercise.getOptionWords()) {
            // TODO(krasikov): use other than first sense.
            options.add(option.getSenses().get(0).getDefinition());
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

    private Word[] GenerateOptions(Word word) {
        Word[] optionWords = new Word[DEFINITION_EXERCISE_OPTIONS_NUM];
        // Later correct option will be moved to a random position.
        optionWords[0] = word;
        for (int i = 1; i < DEFINITION_EXERCISE_OPTIONS_NUM; ++i) {
            String candidateOptionRawWord = GetOptionCandidateWord();
            Word candidateOptionWord = wordDao.getWord(candidateOptionRawWord);
            while (candidateOptionRawWord.equals(word.getWord()) ||
                    candidateOptionWord.IsSynonymOf(word.getWord())) {
                candidateOptionRawWord = GetOptionCandidateWord();
                candidateOptionWord = wordDao.getWord(candidateOptionRawWord);
            }

            optionWords[i] = candidateOptionWord;
        }
        return optionWords;
    }

    private String GetOptionCandidateWord() {
        // TODO(krasikov): user something smarter.
        return WordQueue.getInstance().getWordsInProgress().get(new Random().nextInt(100));
    }
}

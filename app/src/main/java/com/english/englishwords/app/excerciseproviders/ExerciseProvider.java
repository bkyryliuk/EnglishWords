package com.english.englishwords.app.excerciseproviders;

import com.english.englishwords.app.data_model.Exercise;

/**
 * Created by rage on 11.05.14.
 */
//TODO(krasikov): add RootExerciseProvider and SynonymExerciseProvider
public interface ExerciseProvider {
  Exercise generateExerciseForWord(String wordToLearn);
}

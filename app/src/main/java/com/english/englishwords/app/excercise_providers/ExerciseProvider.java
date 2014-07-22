package com.english.englishwords.app.excercise_providers;

import com.english.englishwords.app.data_model.Exercise;

/**
 * Created by rage on 11.05.14.
 */
//TODO(krasikov): add SynonymExerciseProvider and RootExerciseProvider (it will combine
// SynonymExerciseProvider and DefinitionExerciseProvider).
public interface ExerciseProvider {
  Exercise generateExerciseForWord(String wordToLearn);
}

package com.english.englishwords.app.dao;

import java.util.List;
import java.util.Set;

public interface WordListsDAO {
  void saveLearnedWords(Set<String> learnedWords);
  Set<String> getLearnedWords();
  List<String> getWordOrderByUsage();
  int getPositionInOriginalWordList(String word);
}

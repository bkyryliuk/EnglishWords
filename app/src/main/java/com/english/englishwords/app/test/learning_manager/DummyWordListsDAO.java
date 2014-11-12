package com.english.englishwords.app.test.learning_manager;

import com.english.englishwords.app.dao.WordListsDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DummyWordListsDAO implements WordListsDAO {

  private final List<String> wordOrder;

  DummyWordListsDAO(List<String> wordOrder) {
    this.wordOrder = wordOrder;
  }

  @Override
  public void saveLearnedWords(Set<String> learnedWords) {

  }

  @Override
  public Set<String> getLearnedWords() {
    return new HashSet<String>();
  }

  @Override
  public List<String> getWordOrderByUsage() {
    return wordOrder;
  }

  @Override
  public int getPositionInOriginalWordList(String word) {
    return wordOrder.indexOf(word);
  }
}

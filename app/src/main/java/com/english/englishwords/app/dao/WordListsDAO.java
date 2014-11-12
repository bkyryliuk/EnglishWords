package com.english.englishwords.app.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public interface WordListsDAO {
  void saveLearnedWords(Set<String> learnedWords);
  InputStream getLearnedWordsStream() throws FileNotFoundException;
  InputStream getOriginalWordOrderStream() throws IOException;
}

package com.english.englishwords.app.dao;

import com.english.englishwords.app.data_model.Word;

/**
 * Created by rage on 11.05.14.
 */
public interface WordDAO {
  Word getWord(String word);
}

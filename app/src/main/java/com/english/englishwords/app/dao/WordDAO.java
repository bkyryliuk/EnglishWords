package com.english.englishwords.app.dao;

import com.english.englishwords.app.pojo.WordValue;

/**
 * Created by rage on 11.05.14.
 */
public interface WordDAO {
  WordValue getWord(String word);
}

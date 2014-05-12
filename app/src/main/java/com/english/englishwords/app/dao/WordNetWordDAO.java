package com.english.englishwords.app.dao;

import com.english.englishwords.app.pojo.Word;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.dictionary.Dictionary;

/**
 * Created by rage on 11.05.14.
 */
public class WordNetWordDAO implements WordDAO {
  @Override
  public Word getWord(String word) {
    Dictionary dic;
    Word wordValue = new Word(word);
    try {
      dic = Dictionary.getFileBackedInstance("data");
      IndexWordSet indexWordSet = dic.lookupAllIndexWords(word);
      for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
        System.out.println(indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
      }
    } catch (JWNLException e) {
    }
    return wordValue;
  }
}

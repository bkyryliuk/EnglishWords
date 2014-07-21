package com.english.englishwords.app.dao;

import android.content.Context;
import android.util.Log;

import com.english.englishwords.app.pojo.Word;
import com.english.englishwords.app.pojo.WordSense;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * Created by rage on 11.05.14.
 */
public class WordNetWordDAO extends WordDAO {
  private Dictionary dictionary;

  public WordNetWordDAO(Context context) {
    super(context);
    dictionary = null;
    try {
      dictionary = Dictionary.getFileBackedInstance(context.getFilesDir().toString() + "/wordnet");
    } catch (JWNLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Word getWord(String wordString) {
    Word word = new Word(wordString);
    try {
      IndexWordSet indexWordSet = dictionary.lookupAllIndexWords(wordString);
      for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
        System.out.println(indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
        ArrayList<String> synonyms = new ArrayList<String>();
        synonyms.add("synonym " + wordString);
        ArrayList<String> examples = new ArrayList<String>();
        examples.add("example " + wordString);
        String definition = wordString + " definition.";
        if (indexWord.getSenses().size() > 0) {
          // TODO make this line working
          definition = indexWord.getSenses().get(0).getGloss();
        } else {
          Log.v(this.getClass().toString(), "no senses in the word" + wordString);
        }
        word.getSenses().add(new WordSense(wordString, definition, examples, synonyms));
      }
    } catch (JWNLException e) {
      // TODO fix this
      ArrayList<String> synonyms = new ArrayList<String>();
      synonyms.add(wordString + "blabla");
      ArrayList<String> examples = new ArrayList<String>();
      examples.add(wordString + "strange blabla");
      WordSense sense = new WordSense(wordString, wordString + "definition", synonyms, examples);
      word.getSenses().add(sense);
    }
    return word;
  }
}

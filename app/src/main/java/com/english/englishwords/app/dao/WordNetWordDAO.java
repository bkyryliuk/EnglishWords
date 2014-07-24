package com.english.englishwords.app.dao;

import android.content.Context;
import android.util.Log;

import com.english.englishwords.app.data_model.Word;
import com.english.englishwords.app.data_model.WordSense;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.dictionary.Dictionary;

import java.util.ArrayList;

/**
 * Created by rage on 11.05.14.
 */
public class WordNetWordDAO implements WordDAO {
  private Dictionary dictionary = null;

  public WordNetWordDAO(Context context) {
    try {
      dictionary = Dictionary.getFileBackedInstance(context.getFilesDir().toString() + "/wordnet");
    } catch (JWNLException e) {
      e.printStackTrace();
    }
  }

  public String extractSubstring(String gloss, Boolean inQuotes) {
    int quoteIndex = gloss.indexOf("\"");
    if (quoteIndex < 0) {
      quoteIndex = gloss.length();
    }
    return inQuotes ? gloss.substring(quoteIndex): gloss.substring(0, quoteIndex);
  }

  @Override
  public Word getWord(String wordString) {
    long startTime = System.currentTimeMillis();
    Word word = new Word(wordString);
    try {
      IndexWordSet indexWordSet = dictionary.lookupAllIndexWords(wordString);
      for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
        System.out.println(indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
        if (indexWord.getSenses().size() == 0) {
          Log.v(this.getClass().toString(), "no senses for the word '" + wordString + "'");
        }
        // Note(krasikov): take only first synset for now.
        Synset synset = indexWord.getSenses().get(0);
        String definition;
        ArrayList<String> synonyms = new ArrayList<String>();
        for (net.sf.extjwnl.data.Word synonym : synset.getWords()) {
          synonyms.add(synonym.getLemma());
        }
        ArrayList<String> examples = new ArrayList<String>();
        examples.add(extractSubstring(synset.getGloss(), true));
        definition = extractSubstring(synset.getGloss(), false);
        word.getSenses().add(new WordSense(wordString, definition, examples, synonyms));
      }
    } catch (JWNLException e) {
      e.printStackTrace();
      assert false;
    }
    Log.d("WordNetWordRetrieval", (System.currentTimeMillis() - startTime) + " milliseconds were spend on a word retrieval.");
    return word;
  }
}

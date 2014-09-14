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
import java.util.PriorityQueue;

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
    return inQuotes ? gloss.substring(quoteIndex).replaceAll("\"", "") :
                      gloss.substring(0, quoteIndex);
  }

  @Override
  public Word getWord(String wordString) {
    long startTime = System.currentTimeMillis();
    Word word = new Word(wordString);
    // Note(krasikov): take only a single best synset for now.
    addSense(word, getMostFrequentSynset(wordString));
    Log.d("WordNetWordRetrieval", (System.currentTimeMillis() - startTime) + " milliseconds were spend on a word retrieval.");
    return word;
  }

  private Synset getMostFrequentSynset(String wordString) {
    Synset mostFrequentSynset = null;
    int bestUseCount = -1;
    try {
      IndexWordSet indexWordSet = dictionary.lookupAllIndexWords(wordString);
      for (IndexWord indexWord : indexWordSet.getIndexWordArray()) {
        System.out.println(indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
        if (indexWord.getSenses().size() == 0) {
          Log.e(getClass().getCanonicalName(), "no senses for the word '" + wordString + "'");
        }
        for (Synset synset : indexWord.getSenses()) {
          Log.v(getClass().getCanonicalName(), "the synset is " + synset.toString());
          int synsetUseCount = 0;  // sum of the use of all words with the same lemma of the synset
          for(net.sf.extjwnl.data.Word wordFromSynset : synset.getWords()) {
            if(indexWord.getLemma().contentEquals(wordFromSynset.getLemma())) {
              synsetUseCount += wordFromSynset.getUseCount();
            }
          }
          if (bestUseCount < synsetUseCount) {
            bestUseCount = synsetUseCount;
            mostFrequentSynset = synset;
          }
          // TODO(Bogdan) make fancy logging to accumulate incorrect data.
          // log unusual cases where actual word is not found in the synset.
          // may happen for instance for the verb forms like be and was.
          if (synset.indexOfWord(wordString) == -1) {
              Log.e(getClass().getCanonicalName(), "word " + wordString + " cannot be found in the synset " + synset.toString());
          }
        }
      }
    } catch (JWNLException e) {
      e.printStackTrace();
      assert false;
    }
    return mostFrequentSynset;
  }

  private void addSense(Word word, Synset synset) {
    String definition;
    ArrayList<String> synonyms = new ArrayList<String>();
    if (synset == null) {
        Log.e(getClass().getCanonicalName(), "Synset is null for the word " + word.getWord());
    }
    for (net.sf.extjwnl.data.Word synonym : synset.getWords()) {
      synonyms.add(synonym.getLemma());
    }
    ArrayList<String> examples = new ArrayList<String>();
    examples.add(extractSubstring(synset.getGloss(), true));
    definition = extractSubstring(synset.getGloss(), false);
    word.getSenses().add(new WordSense(word.getWord(), definition, examples, synonyms));
  }
}

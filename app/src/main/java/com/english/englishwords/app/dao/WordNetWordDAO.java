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
        Log.d(getClass().getCanonicalName(),
            indexWord.getLemma() + " " + indexWord.getPOS() + " " + indexWord.getSenses().size());
        // TODO(krasikov): remove this check - presumably wordnet never returns index word with no
        // synsets.
        if (indexWord.getSenses().size() == 0) {
          Log.e(getClass().getCanonicalName(), "no senses for the word '" + wordString + "'");
          throw new IllegalArgumentException();
        }
        for (Synset synset : indexWord.getSenses()) {
          int wordIndex = synset.indexOfWord(wordString);
          if (wordIndex == -1) {
            // This can happen when original wordString e.g. 'dos' gets converted to another lemma
            // e.g. 'do'. So we need to ignore index words.
            break;
          }
          // Get synset for which word has the most frequent use.
          int useCount = synset.getWords().get(wordIndex).getUseCount();
          if (bestUseCount < useCount) {
            bestUseCount = useCount;
            mostFrequentSynset = synset;
          }
        }
      }
    } catch (JWNLException e) {
      e.printStackTrace();
      // exit application - maybe upon restart it will work? =)
      throw new IllegalArgumentException();
    }
    return mostFrequentSynset;
  }

  private void addSense(Word word, Synset synset) {
    String definition;
    ArrayList<String> synonyms = new ArrayList<String>();
    for (net.sf.extjwnl.data.Word synonym : synset.getWords()) {
      synonyms.add(synonym.getLemma());
    }
    ArrayList<String> examples = new ArrayList<String>();
    examples.add(extractSubstring(synset.getGloss(), true));
    definition = extractSubstring(synset.getGloss(), false);
    word.getSenses().add(new WordSense(word.getWord(), definition, examples, synonyms));
  }
}

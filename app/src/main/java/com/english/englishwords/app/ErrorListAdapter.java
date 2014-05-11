package com.english.englishwords.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.english.englishwords.app.pojo.WordValue;

/**
 * Created by bogdank on 4/6/14.
 */
public class ErrorListAdapter extends BaseAdapter {
  WordValue[] wordValues;
  private static LayoutInflater inflater = null;

  public ErrorListAdapter(LayoutInflater i, WordValue[] wss) {
    wordValues = wss;
    inflater = i;
  }

  public int getCount() {
    return wordValues.length;
  }

  public Object getItem(int position) {
    return position;
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    View vi = convertView;
    if (convertView == null)
      vi = inflater.inflate(R.layout.error_list_view, null);

    WordValue wordValue = wordValues[position];
    TextView wordTextView = (TextView) vi.findViewById(R.id.error_exercise_word);
    wordTextView.setText(wordValue.getWord());
    TextView definitionTextView = (TextView) vi.findViewById(R.id.error_word_definition_text);
    definitionTextView.setText(wordValue.getSenses().get(0).getDefinition());
    TextView examplesTextView = (TextView) vi.findViewById(R.id.error_word_examples_text);
    examplesTextView.setText(wordValue.getSenses().get(0).getConcatenatedExamples());
    TextView synonymsTextView = (TextView) vi.findViewById(R.id.error_word_synonyms_text);
    synonymsTextView.setText(wordValue.getSenses().get(0).getConcatenatedSynonyms());

    // TODO fill data from the exerice
    return vi;
  }
}

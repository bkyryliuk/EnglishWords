package com.english.englishwords.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.english.englishwords.app.pojo.Word;

/**
 * Created by bogdank on 4/6/14.
 */
class ErrorListAdapter extends BaseAdapter {
  private static LayoutInflater inflater = null;
  private final Word[] words;

  public ErrorListAdapter(LayoutInflater i, Word[] wss) {
    words = wss;
    inflater = i;
  }

  public int getCount() {
    return words.length;
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

    Word word = words[position];
    TextView wordTextView = (TextView) vi.findViewById(R.id.error_exercise_word);
    wordTextView.setText(word.getWord());
    TextView definitionTextView = (TextView) vi.findViewById(R.id.error_word_definition_text);
    definitionTextView.setText(word.getSenses().get(0).getDefinition());
    TextView examplesTextView = (TextView) vi.findViewById(R.id.error_word_examples_text);
    examplesTextView.setText(word.getSenses().get(0).getConcatenatedExamples());
    TextView synonymsTextView = (TextView) vi.findViewById(R.id.error_word_synonyms_text);
    synonymsTextView.setText(word.getSenses().get(0).getConcatenatedSynonyms());

    // TODO fill data from the exerice
    return vi;
  }
}

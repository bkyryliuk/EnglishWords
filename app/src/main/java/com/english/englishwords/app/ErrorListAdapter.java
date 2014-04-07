package com.english.englishwords.app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.WordSense;

/**
 * Created by bogdank on 4/6/14.
 */
public class ErrorListAdapter extends BaseAdapter {
    WordSense[] wordSenses;
    private static LayoutInflater inflater=null;

    public ErrorListAdapter(LayoutInflater i, WordSense[] wss) {
        wordSenses = wss;
        inflater = i;
    }

    public int getCount() {
        return wordSenses.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.error_list_view, null);

        WordSense wordSense = wordSenses[position];
        TextView wordTextView = (TextView) vi.findViewById(R.id.error_exercise_word);
        wordTextView.setText(wordSense.getWord());
        TextView definitionTextView = (TextView) vi.findViewById(R.id.error_word_definition_text);
        definitionTextView.setText(wordSense.getDefinition());
        TextView examplesTextView = (TextView) vi.findViewById(R.id.error_word_examples_text);
        examplesTextView.setText(wordSense.getConcatenatedExamples());
        TextView synonymsTextView = (TextView) vi.findViewById(R.id.error_word_synonyms_text);
        synonymsTextView.setText(wordSense.getConcatenatedSynonyms());

        // TODO fill data from the exerice
        return vi;
    }
}

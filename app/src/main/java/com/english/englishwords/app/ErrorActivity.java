package com.english.englishwords.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.english.englishwords.app.pojo.Word;


public class ErrorActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_error);
    Intent myIntent = getIntent();
    Word correctAnswer = (Word) myIntent.getSerializableExtra("correct answer");
    Word clickedAnswer = (Word) myIntent.getSerializableExtra("clicked answer");
    Bundle bundle = new Bundle();
    bundle.putSerializable("correct answer", correctAnswer);
    bundle.putSerializable("clicked answer", clickedAnswer);
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction()
        .replace(R.id.error_container, ErrorFragment.newInstance(bundle))
        .commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.error, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class ErrorFragment extends Fragment {
      public ErrorFragment() {
    }

      /**
       * Returns a new instance of this fragment for the given section
       * number.
       */
      public static ErrorFragment newInstance(Bundle args) {
          ErrorFragment fragment = new ErrorFragment();
          fragment.setArguments(args);
          return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View rootView = inflater.inflate(R.layout.fragment_error, container, false);
      ListView errorListView = (ListView) rootView.findViewById(R.id.error_list_view);
      Word correctAnswer = (Word) getArguments().getSerializable("correct answer");
      Word clickedAnswer = (Word) getArguments().getSerializable("clicked answer");
      Word[] words = new Word[2];
      words[0] = correctAnswer;
      words[1] = clickedAnswer;

      ErrorListAdapter errorListAdapter = new ErrorListAdapter(inflater, words);
      errorListView.setAdapter(errorListAdapter);
      return rootView;
    }

  }
}

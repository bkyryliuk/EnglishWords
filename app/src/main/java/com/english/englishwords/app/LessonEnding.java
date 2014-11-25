package com.english.englishwords.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.english.englishwords.app.dao.WordListsDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.WordStats;
import com.english.englishwords.app.learning_manager.LearningManager;

import java.util.ArrayList;

public class LessonEnding extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lesson_ending);

    // TODO(bogdan) set stats

    // NOTE(krasikov): at this point LearningManager should be initialized by MainActivity.
    WordStatsDAO wordStatsDAO = LearningManager.getInstance().getWordStatsDAO();
    WordListsDAO wordListsDAO = LearningManager.getInstance().getWordListsDAO();

    Log.v(
        this.getClass().getCanonicalName(),
        "Current position is " + Integer.toString(wordListsDAO.getLearnedWords().size()));

    TextView word_position_text_view =
        (TextView) findViewById(R.id.activity_lesson_ending_position_number);
    word_position_text_view.setText(Integer.toString(wordListsDAO.getLearnedWords().size()));

    Bundle bundle = getIntent().getExtras();
    ArrayList<String> wordsInLesson = (ArrayList<String>) bundle.getSerializable(
        MainActivity.LearningFragment.WORDS_IN_LESSON);
    //ArrayList<String> wordsInLesson = (ArrayList<String>) savedInstanceState.getSerializable(
    //        MainActivity.LearningFragment.WORDS_IN_LESSON);

    int worst_score = Integer.MAX_VALUE;
    String hardest_word = "";
    for (String word : wordsInLesson) {
      Log.d(this.getClass().getCanonicalName(), "The word processed in the exercise is " + word);
      WordStats stats = wordStatsDAO.getStats(word);
      int score = 0;
      // TODO(bogdan): this should be limited by the time from the session start otherwise lifetime
      // stats are used.
      for (Pair<Long, Boolean> result : stats.history) {
        if (result.second) {
          score += 2;
        } else {
          score -= 1;
        }
      }
      if (worst_score > score) {
        worst_score = score;
        hardest_word = word;
      }
      // TODO(bogdan) get the best stats
    }
    TextView hardest_word_test_view = (TextView) findViewById(R.id.activity_lesson_ending_hardest_word_value);
    hardest_word_test_view.setText(hardest_word);

    TextView word_score = (TextView) findViewById(R.id.activity_lesson_ending_hardest_word_score);
    word_score.setText(Integer.toString(worst_score));

    Button startNewLessonButton = (Button) findViewById(R.id.activity_lesson_ending_start_new_lesson);
    startNewLessonButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
      }
    });
    // TODO(Bogdan) implement the listeners for exit to main menu and show detailed stats
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.lesson_ending, menu);
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
}

package com.english.englishwords.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.english.englishwords.app.data_model.LearningManager;


public class InitialTest extends Activity {

  private static final String INITIAL_TEST_PASSED_MARKER = "initial_test_passed";

  public static boolean isInitialTestNeeded(Context context) {
    return !PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
        INITIAL_TEST_PASSED_MARKER, false);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_initial_test);
    Log.d(getClass().getCanonicalName(), "in InitialTest#onCreate");

    Button levelSubmitButton = (Button) findViewById(R.id.level_submit_button);
    levelSubmitButton.setOnClickListener(new Button.OnClickListener() {
      public void onClick(View v) {
        EditText text = (EditText) findViewById(R.id.user_level);
        Log.d(getClass().getCanonicalName(), text.getText().toString());
        Log.d(getClass().getCanonicalName(),
            "Learned words count: " + Integer.toString(
                LearningManager.getInstance().getLearnedWordsNum()));
        if (text.getText().toString().isEmpty()) {
          LearningManager.getInstance().pretendUserLearnedFirstNWords(0);
        } else {
          Log.d(getClass().getCanonicalName(),
              "Setting up " + Integer.toString(
                  Integer.parseInt(text.getText().toString())));
          LearningManager.getInstance().pretendUserLearnedFirstNWords(
              Integer.parseInt(text.getText().toString()));
        }

        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).
            edit().putBoolean(INITIAL_TEST_PASSED_MARKER, true).commit();

        Intent intent = new Intent(v.getContext(), MainActivity.class);
        startActivity(intent);
      }
    });
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.initial_test, menu);
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

package com.english.englishwords.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.english.englishwords.app.data_model.WordQueue;


public class InitialTest extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_test);

        Button buttonOne = (Button) findViewById(R.id.level_submit_button);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                EditText text = (EditText) findViewById(R.id.user_level);
                Log.e(getClass().getCanonicalName(), text.getText().toString());
                Log.e(getClass().getCanonicalName(),
                        "curent position is " + Integer.toString(WordQueue.getInstance().GetPosition()));
                if (text.getText().toString().isEmpty()) {
                    WordQueue.getInstance().SetPosition(0);
                } else {
                    Log.e(getClass().getCanonicalName(),
                            "Setting up " + Integer.toString(
                                    Integer.parseInt(text.getText().toString())));
                    WordQueue.getInstance().SetPosition(Integer.parseInt(text.getText().toString()));
                }

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

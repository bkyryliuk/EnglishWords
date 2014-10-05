package com.english.englishwords.app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.english.englishwords.app.dao.WordNetWordDAO;
import com.english.englishwords.app.dao.WordStatsDAO;
import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.data_model.WordQueue;
import com.english.englishwords.app.excercise_providers.DefinitionExerciseManager;
import com.english.englishwords.app.excercise_providers.ExerciseManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {

  /**
   * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
   */
  private NavigationDrawerFragment attachedFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize singleton for word queue from original_word_order.txt or from local storage
    // (if application was in use before).
      if (WordQueue.getInstance() == null) {
          WordQueue.initialize(getApplicationContext());
      }

    setContentView(R.layout.activity_main);

    getActionBar().hide();

    attachedFragment = (NavigationDrawerFragment)
        getFragmentManager().findFragmentById(R.id.navigation_drawer);
    attachedFragment.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));
      if (WordQueue.getInstance().GetPosition() <= 0) {
          Intent intent = new Intent(this.getApplicationContext(), InitialTest.class);
          startActivity(intent);
          return;
      }
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // update the main content by replacing fragments
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(
            R.id.container, LearningFragment.newInstance(position + 1)).commit();
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class LearningFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
      private static final int lessonSize = 6;
    private static int exerciseNumInCurrentSession = 0;
    // updates to this arraylist will update the list view on the screen that is
    // responsible for displaying possible choices
    private final ArrayList<String> options = new ArrayList<String>();
      private ExerciseManager exerciseManager;
    private Exercise exercise = null;

      public LearningFragment() {
      // TODO(Bogdan) add the empty constructor implementation
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LearningFragment newInstance(int sectionNumber) {
        LearningFragment fragment = new LearningFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

      @Override
      public void onAttach(Activity activity) {
          super.onAttach(activity);
          this.exerciseManager = new DefinitionExerciseManager(
                  new WordNetWordDAO(activity.getApplicationContext()),
                  new WordStatsDAO(activity.getApplicationContext()));

          createNextExercise();
      }

    void OnUserClickedAnOption(View rootView, int position) {
      if (exerciseManager.onAnswerGiven(position, exercise)) {
        exerciseNumInCurrentSession++;
        createNextExercise();
        updateView(rootView);
      } else {
        // change the fragment to display the error screen
        Intent intent = new Intent(rootView.getContext(), ErrorActivity.class);
        // TODO(Bogdan) move strings to the strings.xml
        intent.putExtra("correct answer", exercise.getLearningWord());
        intent.putExtra("clicked answer", exercise.getOptionWords()[position]);
        startActivity(intent);
      }
    }

    //TODO(krasikov): move this method to MemorizationDecider.
    private void createNextExercise() {
      // TODO(krasikov): Pick the word from WordQueue with highest learning priority.
      if (WordQueue.getInstance().getWordsInProgress() == null) {
        System.out.println("words in progress are null");
      }
      List<String> words = WordQueue.getInstance().getWordsInProgress();
      String word = "";
      if (words.size() > exerciseNumInCurrentSession) {
        word = words.get(exerciseNumInCurrentSession);
      } else {
        // TODO(Bogdan) add the congrats activity
        Log.e(this.getClass().getSimpleName(), "Learned all the words!");
      }
        System.out.println("learning: " + word);

        if (exerciseNumInCurrentSession == lessonSize) {
            exerciseNumInCurrentSession = 0;
            // TODO(bogdank) end the lesson and show the stats
        } else {
            exercise = exerciseManager.generateExerciseForWord(word);
        }
    }

    public void updateView(View view) {
      TextView word = (TextView) view.findViewById(R.id.error_exercise_word);
      word.setText(exercise.getQuestion());
      ListView answersListView = (ListView) view.findViewById(R.id.exercise_answers);
      options.clear();
      for (String newAnswer : exercise.getOptions()) {
        options.add(newAnswer);
      }
      ((ArrayAdapter) answersListView.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      ListView answersListView = (ListView) rootView.findViewById(R.id.exercise_answers);
      ArrayAdapter adapter = new ArrayAdapter<String>(rootView.getContext(),
          android.R.layout.simple_list_item_1, options);
      answersListView.setAdapter(adapter);
      // call only after adapter creation
      updateView(rootView);
      // add list actions to the list view
      answersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
          OnUserClickedAnOption(rootView, position);
          // When clicked, show a toast with the TextView text
          Toast.makeText(rootView.getContext(),
              ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
        }
      });
      return rootView;
    }
  }
}

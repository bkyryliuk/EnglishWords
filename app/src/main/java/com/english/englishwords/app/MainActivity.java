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

import com.english.englishwords.app.dao.FileWordListsDAO;
import com.english.englishwords.app.dao.SQLLiteHelper;
import com.english.englishwords.app.dao.WordNetWordDAO;
import com.english.englishwords.app.dao.SQLLiteWordStatsDAO;
import com.english.englishwords.app.data_model.Exercise;
import com.english.englishwords.app.learning_manager.LearningManager;
import com.english.englishwords.app.excercise_providers.DefinitionExerciseManager;
import com.english.englishwords.app.excercise_providers.ExerciseManager;

import java.util.ArrayList;


public class MainActivity extends Activity
    implements NavigationDrawerFragment.NavigationDrawerCallbacks {
  private NavigationDrawerFragment attachedFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    Log.d(getClass().getCanonicalName(), "in MainActivity#onCreate");

    // Initialize singleton for word queue from words_sorted_by_usage.txt or from local storage
    // (if application was in use before).
    if (LearningManager.getInstance() == null) {
      LearningManager.initialize(
          new SQLLiteWordStatsDAO(new SQLLiteHelper(getApplicationContext())),
          new FileWordListsDAO(getApplicationContext()));
    }

    setContentView(R.layout.activity_main);

    getActionBar().hide();

    attachedFragment = (NavigationDrawerFragment)
        getFragmentManager().findFragmentById(R.id.navigation_drawer);
    attachedFragment.setUp(
        R.id.navigation_drawer,
        (DrawerLayout) findViewById(R.id.drawer_layout));

    if (InitialTest.isInitialTestNeeded(getApplicationContext())) {
      // TODO(krasikov): MainActivity is still in the activities stack so if you go back it will
      // be shown - fix this. Probably InitialTest should be default activity, not sure.
      Intent intent = new Intent(getApplicationContext(), InitialTest.class);
      startActivity(intent);
      return;
    }
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {
    // Update the main content by replacing fragments.
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(
        R.id.container, LearningFragment.newInstance(position + 1)).commit();
  }

  public static class LearningFragment extends Fragment {
    public static final String WORDS_IN_LESSON = "words_in_lesson";

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int lessonSize = 6;
    private static int exercisesPassedInCurrentSession = 0;
    // Updates to this ArrayList will update the list view on the screen that is
    // responsible for displaying possible choices.
    private final ArrayList<String> options = new ArrayList<String>();
    private ExerciseManager exerciseManager;
    private Exercise exercise = null;

    public LearningFragment() {
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

      Log.d(getClass().getCanonicalName(), "in LearningFragment#onAttach");

      this.exerciseManager = new DefinitionExerciseManager(
          new WordNetWordDAO(activity.getApplicationContext()), LearningManager.getInstance());

      createNextExercise();
    }

    void OnUserClickedAnOption(View rootView, int position) {
      exercisesPassedInCurrentSession++;
      if (exerciseManager.onAnswerGiven(position, exercise)) {
        createNextExercise();
        updateView(rootView);
      } else {
        // Change the fragment to display the error screen.
        Intent intent = new Intent(rootView.getContext(), ErrorActivity.class);
        // TODO(Bogdan) move strings to the strings.xml
        intent.putExtra("correct answer", exercise.getLearningWord());
        intent.putExtra("clicked answer", exercise.getOptionWords()[position]);
        startActivity(intent);
      }
    }

    private void createNextExercise() {
      String word = LearningManager.getInstance().popWord();
      if (word == null) {
        // TODO(Bogdan) add the congrats activity.
        Log.e(this.getClass().getSimpleName(), "Learned all the words!");
        return;
      }
      System.out.println("learning: " + word);

      if (exercisesPassedInCurrentSession == lessonSize) {
        exercisesPassedInCurrentSession = 0;
        // TODO(bogdank) end the lesson and show the stats
        Intent intent = new Intent(this.getActivity().getApplicationContext(), LessonEnding.class);
        startActivity(intent);
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

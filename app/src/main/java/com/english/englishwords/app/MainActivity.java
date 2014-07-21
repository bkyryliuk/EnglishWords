package com.english.englishwords.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.english.englishwords.app.dao.WordNetWordDAO;
import com.english.englishwords.app.excerciseproviders.DefinitionExerciseProvider;
import com.english.englishwords.app.excerciseproviders.ExerciseProvider;
import com.english.englishwords.app.pojo.Exercise;
import com.english.englishwords.app.pojo.WordQueue;

import java.util.ArrayList;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize singleton for word queue from original_word_order.txt or from local storage
        // (if application was in use before).
        WordQueue.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, LearningFragment.newInstance(this.getApplicationContext(), position + 1))
                .commit();
    }

    void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    public static class LearningFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int exerciseNumInCurrentSession = 0;

        private final ExerciseProvider exerciseProvider;
        // updates to this arraylist will update the list view on the screen that is
        // responsible for displaying possible choices
        private final ArrayList<String> options = new ArrayList<String>();
        private Exercise exercise = null;

        public LearningFragment(Context context) {
            // TODO(Bogdan) add the empty constructor implementation
            this.exerciseProvider = new DefinitionExerciseProvider(
                    new WordNetWordDAO(context));
            //new RandomWordDAO());
            createNextExercise();
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static LearningFragment newInstance(Context context, int sectionNumber) {
            LearningFragment fragment = new LearningFragment(context);
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        void OnUserClickedAnOption(View rootView, int position) {
            if (position != exercise.getCorrectOption()) {
                //TODO(krasikov): update WordStat.
                // change the fragment to display the error screen
                Intent intent = new Intent(rootView.getContext(), ErrorActivity.class);
                intent.putExtra("correct answer", exercise.getLearningWord());
                intent.putExtra("clicked answer", exercise.getOptionWords()[position]);
                startActivity(intent);
            } else {
                //TODO(krasikov): update WordStat.
                exerciseNumInCurrentSession++;
                createNextExercise();
                updateView(rootView);
            }
        }

        //TODO(krasikov): move this method to MemorizationDecider.
        private void createNextExercise() {
            // TODO(krasikov): Pick the word from WordQueue with highest learning priority.
            if (WordQueue.getInstance().getWordsInProgress() == null) {
                System.out.println("words in progress are null");
            }
            String word = WordQueue.getInstance().getWordsInProgress().get(exerciseNumInCurrentSession);
            System.out.println("learn: " + word);
            exercise = exerciseProvider.generateExerciseForWord(word);
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

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}

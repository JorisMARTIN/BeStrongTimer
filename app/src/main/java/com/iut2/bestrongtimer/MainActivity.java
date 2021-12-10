package com.iut2.bestrongtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Training.Training;
import com.iut2.bestrongtimer.utils.LaunchEditDialog;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String SELECTED_TRAINING = "selected_training";

    FragmentManager fm;
    LaunchEditDialog dialog;

    // DATA
    DatabaseClient db;
    List<Training> allTrainings;

    // VIEW
    LinearLayout trainingsContainer;
    TextView anyTrainingAvailable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        trainingsContainer = findViewById(R.id.training_layout_container);
        anyTrainingAvailable = findViewById(R.id.any_trainings_available_text);

        db = DatabaseClient.getInstance(getApplicationContext());

        getTrainings();
    }

    @Override
    protected void onResume() {



        super.onResume();
    }

    @Override
    protected void onPause() {
        dismissDialog();
        super.onPause();
    }

    private void getTrainings() {

        class CollectTrainings extends AsyncTask<Void, Void, List<Training>> {

            @Override
            protected List<Training> doInBackground(Void... voids) {
                List<Training> trainings = db.getAppDatabase().trainingDAO().getAll();
                return trainings;
            }

            @Override
            protected void onPostExecute(List<Training> trainings) {
                super.onPostExecute(trainings);

                // Hide text only if some trainings are available
                if (trainings.size() > 0) {
                    anyTrainingAvailable.setVisibility(View.INVISIBLE);
                }
                allTrainings = trainings;
                // Open dialog if activity was restarted
                initView();
            }
        }

        CollectTrainings collectTrainings = new CollectTrainings();
        collectTrainings.execute();
    }

    private void initView() {

        for (Training training : allTrainings) {
            System.out.println(training.toString());

            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.component_training, null);

            // Inflater views
            TextView title = (TextView) layout.findViewById(R.id.component_training_title);
            TextView description = (TextView) layout.findViewById(R.id.component_training_description);
            TextView date = (TextView) layout.findViewById(R.id.component_training_date);
            TextView time = (TextView) layout.findViewById(R.id.component_training_globalTime);
            TextView level = (TextView) layout.findViewById(R.id.component_training_globalLevel);

            // Apply training data
            title.setText(training.getName());
            description.setText(training.getDescription());
            date.setText(training.getCreationDate().toString());
            time.setText(String.format("%02d min, %02d s",
                    TimeUnit.MILLISECONDS.toMinutes(training.getGlobalSequenceTime() + training.getSetupTime()),
                    TimeUnit.MILLISECONDS.toSeconds(training.getGlobalSequenceTime() + training.getSetupTime()) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(training.getGlobalSequenceTime() + training.getSetupTime()))));

            level.setText(training.getDifficultyAverage() + " / 5");
            // Add event
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(training);
                }
            });

            trainingsContainer.addView(layout);
        }
    }

    private void showDialog(Training training) {
        dismissDialog();

        dialog = LaunchEditDialog.newIntance(training);
        dialog.show(fm, "launch_edit_training");
    }

    private void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
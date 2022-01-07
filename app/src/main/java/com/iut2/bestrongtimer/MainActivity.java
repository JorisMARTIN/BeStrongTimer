package com.iut2.bestrongtimer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iut2.bestrongtimer.AddEditTraining.AddEditTraining;
import com.iut2.bestrongtimer.db.DatabaseClient;
import com.iut2.bestrongtimer.db.Entity.Training.Training;
import com.iut2.bestrongtimer.utils.LaunchEditDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm;
    LaunchEditDialog dialog;

    // DATA
    DatabaseClient db;
    List<Training> allTrainings;

    // VIEW
    LinearLayout trainingsContainer;
    TextView anyTrainingAvailable;
    FloatingActionButton addTrainingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();

        trainingsContainer = findViewById(R.id.training_layout_container);
        anyTrainingAvailable = findViewById(R.id.any_trainings_available_text);
        addTrainingButton = findViewById(R.id.training_add_button);

        addTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddEditTraining.class);
                startActivity(intent);
            }
        });

        db = DatabaseClient.getInstance(getApplicationContext());
    }

    @Override
    protected void onResume() {
        getTrainings();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dismissDialog();
        super.onPause();
    }

    private void getTrainings() {

        trainingsContainer.removeAllViews();

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
                allTrainings = new ArrayList<>(trainings);
                // Open dialog if activity was restarted
                initView();
            }
        }

        CollectTrainings collectTrainings = new CollectTrainings();
        collectTrainings.execute();
    }

    private void initView() {

        for (Training training : allTrainings) {

            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.component_training, null);

            // Inflater views
            TextView title = (TextView) layout.findViewById(R.id.component_training_title);
            TextView description = (TextView) layout.findViewById(R.id.component_training_description);
            TextView date = (TextView) layout.findViewById(R.id.component_training_date);

            // Apply training data
            title.setText(training.getName());
            description.setText(training.getDescription());

            SimpleDateFormat formater = new SimpleDateFormat("dd MMM yyyy hh:mm");

            date.setText(formater.format(training.getCreationDate()));

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
package com.iut2.bestrongtimer.utils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.iut2.bestrongtimer.LiveTraining.LiveTrainingActivity;
import com.iut2.bestrongtimer.R;
import com.iut2.bestrongtimer.db.Entity.Training.Training;

public class LaunchEditDialog extends DialogFragment implements View.OnClickListener {

    private static final String TRAINING = "training";
    // BUTTONS
    Button launchTrainingButton, editTrainingButton;

    // Training
    Training training;

    public LaunchEditDialog() {}

    public static LaunchEditDialog newIntance(Training training) {
        LaunchEditDialog dialog = new LaunchEditDialog();
        Bundle args = new Bundle();
        args.putParcelable("training", training);
        dialog.setArguments(args);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_launch_edit, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        launchTrainingButton = view.findViewById(R.id.pop_up_launch_button);
        editTrainingButton = view.findViewById(R.id.pop_up_edit_button);
        training = getArguments().getParcelable("training");

        launchTrainingButton.setOnClickListener(this);
        editTrainingButton.setOnClickListener(this);
    }

    @Override
    public void onResume() {

        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();

        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        // Call super onResume after sizing
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        dismiss();

        // Launch training
        if (v == launchTrainingButton) {
            Intent intent = new Intent(getContext(), LiveTrainingActivity.class);
            intent.putExtra(TRAINING, training);
            startActivity(intent);
        // Edit training
        } else {

        }
    }
}

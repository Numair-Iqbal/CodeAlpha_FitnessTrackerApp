package com.numair.fitnesstrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView tvDate, tvSteps, tvCalories, tvMinutes;
    ProgressBar progressSteps, progressCalories, progressMinutes;
    Button btnAddActivity;
    ListView lvActivities;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDate = findViewById(R.id.tvDate);
        tvSteps = findViewById(R.id.tvSteps);
        tvCalories = findViewById(R.id.tvCalories);
        tvMinutes = findViewById(R.id.tvMinutes);
        progressSteps = findViewById(R.id.progressSteps);
        progressCalories = findViewById(R.id.progressCalories);
        progressMinutes = findViewById(R.id.progressMinutes);
        btnAddActivity = findViewById(R.id.btnAddActivity);
        lvActivities = findViewById(R.id.lvActivities);

        db = new DatabaseHelper(this);

        // Add Activity button
        btnAddActivity.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddActivityActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboard();
    }

    private void loadDashboard() {
        // Aaj ki date
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String displayDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(new Date());
        tvDate.setText("Today: " + displayDate);

        // Aaj ke totals lo
        int[] totals = db.getTotalsForDate(today);
        int steps = totals[0];
        int calories = totals[1];
        int minutes = totals[2];

        // Summary update karo
        tvSteps.setText(steps + " / 10,000 steps");
        tvCalories.setText(calories + " / 500 kcal");
        tvMinutes.setText(minutes + " / 60 mins");

        // Progress bars update karo
        progressSteps.setProgress(Math.min(steps, 10000));
        progressCalories.setProgress(Math.min(calories, 500));
        progressMinutes.setProgress(Math.min(minutes, 60));

        // Recent activities list
        List<FitnessEntry> allEntries = db.getAllEntries();
        ArrayList<String> displayList = new ArrayList<>();

        if (allEntries.isEmpty()) {
            displayList.add("No activities logged yet. Press + Log Activity!");
        } else {
            for (FitnessEntry entry : allEntries) {
                displayList.add(
                        "📅 " + entry.getDate() +
                                "  |  🏃 " + entry.getExerciseType() +
                                "\n👟 " + entry.getSteps() + " steps" +
                                "  |  🔥 " + entry.getCalories() + " kcal" +
                                "  |  ⏱️ " + entry.getWorkoutMinutes() + " mins"
                );
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        lvActivities.setAdapter(adapter);
    }
}
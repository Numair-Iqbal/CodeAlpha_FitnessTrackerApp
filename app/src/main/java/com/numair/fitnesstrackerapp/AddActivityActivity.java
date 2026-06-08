package com.numair.fitnesstrackerapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivityActivity extends AppCompatActivity {

    EditText etExerciseType, etSteps, etCalories, etMinutes;
    Button btnSave, btnDelete;
    TextView tvTitle;
    DatabaseHelper db;
    int entryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etExerciseType = findViewById(R.id.etExerciseType);
        etSteps = findViewById(R.id.etSteps);
        etCalories = findViewById(R.id.etCalories);
        etMinutes = findViewById(R.id.etMinutes);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        db = new DatabaseHelper(this);

        // Edit mode check
        if (getIntent().hasExtra("id")) {
            entryId = getIntent().getIntExtra("id", -1);
            etExerciseType.setText(getIntent().getStringExtra("exerciseType"));
            etSteps.setText(String.valueOf(getIntent().getIntExtra("steps", 0)));
            etCalories.setText(String.valueOf(getIntent().getIntExtra("calories", 0)));
            etMinutes.setText(String.valueOf(getIntent().getIntExtra("minutes", 0)));
            btnSave.setText("Update Activity");
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String exerciseType = etExerciseType.getText().toString().trim();
            String stepsStr = etSteps.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();
            String minutesStr = etMinutes.getText().toString().trim();

            if (exerciseType.isEmpty()) {
                Toast.makeText(this, "Please enter exercise type!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (stepsStr.isEmpty()) stepsStr = "0";
            if (caloriesStr.isEmpty()) caloriesStr = "0";
            if (minutesStr.isEmpty()) minutesStr = "0";

            int steps = Integer.parseInt(stepsStr);
            int calories = Integer.parseInt(caloriesStr);
            int minutes = Integer.parseInt(minutesStr);

            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            if (entryId == -1) {
                db.addEntry(today, exerciseType, steps, calories, minutes);
                Toast.makeText(this, "Activity saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                db.updateEntry(entryId, exerciseType, steps, calories, minutes);
                Toast.makeText(this, "Activity updated successfully!", Toast.LENGTH_SHORT).show();
            }
            finish();
        });

        btnDelete.setOnClickListener(v -> {
            db.deleteEntry(entryId);
            Toast.makeText(this, "Activity deleted successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
package com.numair.fitnesstrackerapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddActivityActivity extends AppCompatActivity {

    EditText etExerciseType, etSteps, etCalories, etMinutes;
    Button btnSave, btnCancel;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etExerciseType = findViewById(R.id.etExerciseType);
        etSteps = findViewById(R.id.etSteps);
        etCalories = findViewById(R.id.etCalories);
        etMinutes = findViewById(R.id.etMinutes);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        db = new DatabaseHelper(this);

        // Save button
        btnSave.setOnClickListener(v -> {
            String exerciseType = etExerciseType.getText().toString().trim();
            String stepsStr = etSteps.getText().toString().trim();
            String caloriesStr = etCalories.getText().toString().trim();
            String minutesStr = etMinutes.getText().toString().trim();

            // Validation
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

            // Aaj ki date lo
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            db.addEntry(today, exerciseType, steps, calories, minutes);
            Toast.makeText(this, "Activity saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Cancel button
        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }
}
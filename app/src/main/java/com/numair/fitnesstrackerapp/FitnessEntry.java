package com.numair.fitnesstrackerapp;

public class FitnessEntry {
    private int id;
    private String date;
    private String exerciseType;
    private int steps;
    private int calories;
    private int workoutMinutes;

    public FitnessEntry(int id, String date, String exerciseType, int steps, int calories, int workoutMinutes) {
        this.id = id;
        this.date = date;
        this.exerciseType = exerciseType;
        this.steps = steps;
        this.calories = calories;
        this.workoutMinutes = workoutMinutes;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getExerciseType() { return exerciseType; }
    public int getSteps() { return steps; }
    public int getCalories() { return calories; }
    public int getWorkoutMinutes() { return workoutMinutes; }
}
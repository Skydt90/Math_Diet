package com.skydt.weightcontrol.database;

import android.provider.BaseColumns;

public class DBContract
{
    private DBContract() {}

    public static class DietEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "diets";
        public static final String DIET_ID = "diet_id";
        public static final String DIET_NAME = "diet_name";
        public static final String START_WEIGHT = "start_weight";
        public static final String DESIRED_WEIGHT = "desired_weight";
        public static final String NUMBER_OF_DAYS = "number_of_days";
        public static final String HEIGHT = "height";
        public static final String PAUSED = "paused";
    }

    public static class DayEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "days";
        public static final String DAY_ID = "day_id";
        public static final String DIET_ID = "diet_id";
        public static final String GOAL_WEIGHT = "goal_weight";
        public static final String MORNING_WEIGHT = "morning_weight";
        public static final String ALLOWED_FOOD_INTAKE = "allowed_food_intake";
        public static final String LIKE_DISLIKE = "like_dislike";
    }

    public static class FoodWeightEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "food_weigh_ins";
        public static final String FOOD_ID = "food_id";
        public static final String DAY_ID = "day_id";
        public static final String DIET_ID = "diet_id";
        public static final String WEIGHT = "weight";
        public static final String MEAL_TYPE = "meal_type";
    }

    public static class BodyWeightEntries implements BaseColumns
    {
        public static final String TABLE_NAME = "body_weigh_ins";
        public static final String BODY_ID = "body_id";
        public static final String DAY_ID = "day_id";
        public static final String DIET_ID = "diet_id";
        public static final String WEIGHT = "weight";
    }
}
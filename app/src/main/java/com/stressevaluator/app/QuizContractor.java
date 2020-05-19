package com.stressevaluator.app;

import android.provider.BaseColumns;

public final class QuizContractor {
    // container class for database. No one can create an instance or inherit it
    private QuizContractor() {}

    // create a class for every table
    public static class QuestionTable implements BaseColumns {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String COLUMN_QUESTION = "question";
        // public static final String COLUMN_OPTION1 = "option1";
        // public static final String COLUMN_ANSWER_NR = "answer_nr";
    }
}

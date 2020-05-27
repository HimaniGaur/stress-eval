package com.stressevaluator.app;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;

public class ResponseLocalStore {
    SharedPreferences responseLocalDatabase;
    
    public ResponseLocalStore(Context context, User user) {
        String SP_NAME = user.getUsername() + "_responses";
        responseLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public Integer getQuestionnairesCompletedCounter () {
        return responseLocalDatabase.getInt("questionnaires_completed_counter", 0);
    }

    public void setQuestionnairesCompletedCounter(Integer value) {
        SharedPreferences.Editor spEditor = responseLocalDatabase.edit();
        spEditor.putInt("questionnaires_completed_counter", value);
        spEditor.commit();
    }

    public void setQuestionnaireResponse(String questionnaire, Integer[] response) {
        SharedPreferences.Editor spEditor = responseLocalDatabase.edit();
        Integer score = new Integer(0);
        for (Integer i: response) {
            score += i;
        }
        spEditor.putString(questionnaire + "_response", Arrays.toString(response));
        spEditor.putInt(questionnaire + "_score", score);
        spEditor.commit();
    }

    public Boolean isQuestionnaireAttempted(String qName) {
        return responseLocalDatabase.contains(qName + "_score");
    }

}

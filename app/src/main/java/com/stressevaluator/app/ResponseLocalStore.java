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
        spEditor.apply();
    }

    public void setQuestionnaireResponse(String questionnaireCode, Integer[] response) {
        SharedPreferences.Editor spEditor = responseLocalDatabase.edit();
        Integer score = new Integer(0);
        for (Integer i: response) {
            score += i;
        }
        spEditor.putString(questionnaireCode + "_response", Arrays.toString(response));
        spEditor.putInt(questionnaireCode + "_score", score);
        spEditor.apply();
    }

    public String getQuestionnaireResponse(String questionnaireCode) {

        return responseLocalDatabase.getString(questionnaireCode + "_response", "");

    }

    public Boolean isQuestionnaireAttempted(String qName) {
        return responseLocalDatabase.contains(qName + "_response");
    }

    public void setResponseId(Integer id) {
        SharedPreferences.Editor spEditor = responseLocalDatabase.edit();
        spEditor.putInt("response_id", id);
        spEditor.apply();
    }

    public Integer getResponseId() {
        return responseLocalDatabase.getInt("response_id", 0);
    }

}

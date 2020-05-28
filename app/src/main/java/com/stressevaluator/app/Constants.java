package com.stressevaluator.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
//        public static final String baseUrl = "https://self-important-real.000webhostapp.com/survey";
        public static final String baseUrl = "http://192.168.43.77/survey";
        public static final List<String> questionnaireNames = new ArrayList<>
                (Arrays.asList("BUSARI Stress Scale",
                "Questionnaire of academic stress in higher secondary education (QASSE)",
                "Stress in University Students - Work Living Environment Subscale"));
        public Map<String, String> questionnaireCode = new HashMap<>();

        public static String getQuestionnaireCode (String questionnaireLongName) {
                String code = "";
                switch (questionnaireLongName) {
                        case "BUSARI Stress Scale":
                                code = "Busari";
                                break;
                        case "Questionnaire of academic stress in higher secondary education (QASSE)":
                                code = "QASSE";
                                break;
                        case "Stress in University Students - Work Living Environment Subscale":
                                code = "SUS";
                                break;
                }
                return code;
        }

        public static String getShortDesc(String questionnaireCode) {
                String shortDesc = "";
                switch (questionnaireCode) {
                        case "Busari":
                                shortDesc = "1 = None of the time, " +
                                        "2 = A little of the time, " +
                                        "3 = Some of the time, " +
                                        "4 = Most of the time, " +
                                        "5 = All of the time";
                                break;
                        case "SUS":
                                shortDesc = "1 = Strongly disagree, " +
                                        "2 = Disagree, " +
                                        "3 = Neutral, " +
                                        "4 = Agree, " +
                                        "5 = Strongly Agree, "+
                                        "6 = Don't Know (Skip question)";
                                break;
                        case "QASSE":
                                shortDesc = "1 = Very low, " +
                                        "2 = Low, " +
                                        "3 = Neutral, " +
                                        "4 = High, " +
                                        "5 = Very high";
                                break;
                }
                return shortDesc;
        }
}
package com.example.likhit.cha.model;

public class AppListQuestions {

    private String question;
    private int appId;
    private int questionId;

    public AppListQuestions() {
    }

    public AppListQuestions(String question, int appId, int questionId) {
        this.question = question;
        this.appId = appId;
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
}

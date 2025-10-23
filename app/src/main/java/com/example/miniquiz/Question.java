package com.example.miniquiz;

/**
 * Prosta klasa reprezentująca pytanie quizowe.
 */
public class Question {
    private final String questionText;
    private final String optionA;
    private final String optionB;
    private final String optionC;
    private final String correctAnswer;

    public Question(String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}

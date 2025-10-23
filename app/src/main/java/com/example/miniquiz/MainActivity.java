package com.example.miniquiz;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Główna aktywność aplikacji Mini Quiz.
 * Logika:
 * - Po starcie widoczny tytuł, przycisk start i wynik 0.
 * - Po kliknięciu Start losowane 5 pytań z tablicy.
 * - Użytkownik wybiera odpowiedź (A/B/C). Wynik aktualizowany.
 * - Po 5 pytaniach pokazuje się dialog z wynikiem.
 * - Reset zeruje wynik i pozwala zacząć od nowa.
 */
public class MainActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView scoreTextView;
    private TextView questionTextView;
    private Button startButton;
    private Button resetButton;
    private Button answerAButton;
    private Button answerBButton;
    private Button answerCButton;
    private LinearLayout quizContainer;

    private List<Question> allQuestions;
    private List<Question> quizQuestions;
    private int currentIndex;
    private int score;

    private static final int QUIZ_SIZE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja widoków
        titleTextView = findViewById(R.id.titleTextView);
        scoreTextView = findViewById(R.id.scoreTextView);
        questionTextView = findViewById(R.id.questionTextView);
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        answerAButton = findViewById(R.id.answerAButton);
        answerBButton = findViewById(R.id.answerBButton);
        answerCButton = findViewById(R.id.answerCButton);
        quizContainer = findViewById(R.id.quizContainer);

        // Przygotuj wszystkie pytania
        prepareAllQuestions();

        // Początkowy stan
        resetQuizStateUI();

        // Obsługa przycisku Start
        startButton.setOnClickListener(v -> startQuiz());

        // Obsługa przycisku Reset
        resetButton.setOnClickListener(v -> {
            resetQuizState();
            Toast.makeText(MainActivity.this, "Quiz zresetowany", Toast.LENGTH_SHORT).show();
        });

        // Obsługa przycisków odpowiedzi
        answerAButton.setOnClickListener(v -> handleAnswer(answerAButton.getText().toString()));
        answerBButton.setOnClickListener(v -> handleAnswer(answerBButton.getText().toString()));
        answerCButton.setOnClickListener(v -> handleAnswer(answerCButton.getText().toString()));
    }

    /**
     * Wypełnia listę wszystkich dostępnych pytań.
     */
    private void prepareAllQuestions() {
        allQuestions = new ArrayList<>();

        // Przykładowe pytania — możesz dodać więcej
        allQuestions.add(new Question("Stolica Włoch to:", "Rzym", "Paryż", "Madryt", "Rzym"));
        allQuestions.add(new Question("Które zwierzę to król dżungli?", "Słoń", "Lew", "Tygrys", "Lew"));
        allQuestions.add(new Question("Jaki jest kolor nieba w słoneczny dzień?", "Zielony", "Niebieski", "Czerwony", "Niebieski"));
        allQuestions.add(new Question("2 + 2 = ?", "3", "4", "5", "4"));
        allQuestions.add(new Question("Największy kontynent to:", "Afryka", "Europa", "Azja", "Azja"));
        allQuestions.add(new Question("Język używany w Androidzie (klasyczny) to:", "Java", "Swift", "Kotlin", "Java"));
        allQuestions.add(new Question("Która planeta jest najbliżej Słońca?", "Ziemia", "Merkury", "Mars", "Merkury"));
        allQuestions.add(new Question("Ile nóg ma pająk?", "6", "8", "10", "8"));
        allQuestions.add(new Question("Symbol chemiczny wody to:", "H2O", "CO2", "O2", "H2O"));
        allQuestions.add(new Question("Stolicą Polski jest:", "Kraków", "Warszawa", "Gdańsk", "Warszawa"));
        // ... dodaj więcej jeżeli chcesz
    }

    /**
     * Rozpoczyna quiz: losuje pytania i pokazuje pierwsze.
     */
    private void startQuiz() {
        // Losujemy QUIZ_SIZE pytań bez powtórzeń
        quizQuestions = new ArrayList<>(allQuestions);
        Collections.shuffle(quizQuestions);
        if (quizQuestions.size() > QUIZ_SIZE) {
            quizQuestions = quizQuestions.subList(0, QUIZ_SIZE);
        }

        currentIndex = 0;
        score = 0;
        updateScoreText();

        // Pokaż kontener quizu i ukryj przycisk start (opcjonalne)
        quizContainer.setVisibility(View.VISIBLE);
        startButton.setVisibility(View.GONE);

        showCurrentQuestion();
    }

    /**
     * Wyświetla bieżące pytanie i przypisuje teksty do przycisków odpowiedzi.
     */
    private void showCurrentQuestion() {
        if (currentIndex < 0 || currentIndex >= quizQuestions.size()) {
            return;
        }
        Question q = quizQuestions.get(currentIndex);
        questionTextView.setText(q.getQuestionText());
        answerAButton.setText(q.getOptionA());
        answerBButton.setText(q.getOptionB());
        answerCButton.setText(q.getOptionC());
    }

    /**
     * Obsługa wybranej odpowiedzi.
     *
     * @param chosenAnswer tekst wybranej odpowiedzi
     */
    private void handleAnswer(String chosenAnswer) {
        Question current = quizQuestions.get(currentIndex);
        if (current.getCorrectAnswer().equals(chosenAnswer)) {
            score++;
            updateScoreText();
        }

        currentIndex++;
        if (currentIndex < quizQuestions.size()) {
            showCurrentQuestion();
        } else {
            finishQuiz();
        }
    }

    /**
     * Wyświetla komunikat końcowy i ustawia UI w stanie zakończonym.
     */
    private void finishQuiz() {
        // Ukryj pytania
        quizContainer.setVisibility(View.GONE);

        // Pokaż dialog z wynikiem
        new AlertDialog.Builder(this)
                .setTitle("Koniec quizu!")
                .setMessage("Koniec quizu! Twój wynik: " + score + " / " + QUIZ_SIZE)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Po potwierdzeniu pokaż przycisk start ponownie (można rozpocząć od nowa)
                    startButton.setVisibility(View.VISIBLE);
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Aktualizuje napis ze score.
     */
    private void updateScoreText() {
        scoreTextView.setText("Wynik: " + score);
    }

    /**
     * Resetuje stan quizu (logika + UI).
     */
    private void resetQuizState() {
        score = 0;
        currentIndex = 0;
        quizQuestions = null;
        updateScoreText();

        // Przywróć widoczność tak, aby było widać tylko tytuł, start i wynik
        quizContainer.setVisibility(View.GONE);
        startButton.setVisibility(View.VISIBLE);
    }

    /**
     * Ustawienia początkowe UI przy uruchomieniu aplikacji.
     */
    private void resetQuizStateUI() {
        resetQuizState();
    }
}

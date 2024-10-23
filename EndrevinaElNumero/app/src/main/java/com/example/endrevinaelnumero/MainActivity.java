package com.example.endrevinaelnumero;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText etNumber;
    private TextView tvHistory, tvScrollHistory;
    private Button btnValidate;
    private int randomNumber;
    private int attempts = 0;
    private StringBuilder history = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = findViewById(R.id.etNumber);
        tvHistory = findViewById(R.id.tvHistory);
        tvScrollHistory = findViewById(R.id.tvScrollHistory);
        btnValidate = findViewById(R.id.btnValidate);

        generateRandomNumber();

        btnValidate.setOnClickListener(v -> validateNumber());

        etNumber.setOnEditorActionListener((v, actionId, event) -> {
            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                validateNumber();
                return true;
            }
            return false;
        });
    }

    private void generateRandomNumber() {
        Random random = new Random();
        randomNumber = random.nextInt(100) + 1; // Número entre 1 y 100
    }

    private void validateNumber() {
        String userInput = etNumber.getText().toString();
        if (userInput.isEmpty()) {
            Toast.makeText(this, "Please enter a number", Toast.LENGTH_SHORT).show();
            return;
        }

        int userNumber = Integer.parseInt(userInput);
        attempts++;
        if (userNumber < randomNumber) {
            Toast.makeText(this, "The number is greater", Toast.LENGTH_SHORT).show();
        } else if (userNumber > randomNumber) {
            Toast.makeText(this, "The number is smaller", Toast.LENGTH_SHORT).show();
        } else {
            showEndGameDialog();
        }

        history.append("Attempt ").append(attempts).append(": ").append(userNumber).append("\n");
        tvScrollHistory.setText(history.toString());

        etNumber.setText("");

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void showEndGameDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Congratulations!")
                .setMessage("You guessed the number in " + attempts + " attempts!")
                .setPositiveButton("Play Again", (dialog, which) -> resetGame())
                .setCancelable(false)
                .show();
    }

    private void resetGame() {
        generateRandomNumber();
        attempts = 0;
        history.setLength(0);
        tvScrollHistory.setText("");
    }
}

package com.example.mindgrid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];
    private boolean playerXTurn = true;
    private int roundCount = 0;
    private TextView statusText;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);
        GridLayout gridLayout = findViewById(R.id.gridLayout);

        if (gridLayout.getChildCount() < 9) {
            Toast.makeText(this, "GridLayout must have 9 buttons", Toast.LENGTH_LONG).show();
            return;
        }

        int childIndex = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Button button = (Button) gridLayout.getChildAt(childIndex);
                buttons[i][j] = button;
                final int finalI = i;
                final int finalJ = j;
                button.setOnClickListener(v -> handleMove(buttons[finalI][finalJ]));
                childIndex++;
            }
        }

        resetButton.setOnClickListener(v -> resetGame());
    }

    private void handleMove(Button button) {
        if (!button.getText().toString().isEmpty()) {
            return;
        }

        if (playerXTurn) {
            button.setText("X");
            button.setTextColor(ContextCompat.getColor(this, R.color.red));
        } else {
            button.setText("O");
            button.setTextColor(ContextCompat.getColor(this, R.color.green));
        }

        roundCount++;

        if (checkForWin()) {
            showWinner(playerXTurn ? "Player X wins!" : "Player O wins!");
        } else if (roundCount == 9) {
            showWinner("It's a draw!");
        } else {
            playerXTurn = !playerXTurn;
            statusText.setText(playerXTurn ? "Player X's Turn" : "Player O's Turn");
        }
    }


    private boolean checkForWin() {
        String[][] field = new String[3][3];

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                field[i][j] = buttons[i][j].getText().toString();

        for (int i = 0; i < 3; i++) {
            // Rows
            if (field[i][0].equals(field[i][1]) &&
                    field[i][0].equals(field[i][2]) &&
                    !field[i][0].isEmpty()) {
                return true;
            }

            // Columns
            if (field[0][i].equals(field[1][i]) &&
                    field[0][i].equals(field[2][i]) &&
                    !field[0][i].isEmpty()) {
                return true;
            }
        }

        // Diagonals
        if (field[0][0].equals(field[1][1]) &&
                field[0][0].equals(field[2][2]) &&
                !field[0][0].isEmpty()) {
            return true;
        }

        if (field[0][2].equals(field[1][1]) &&
                field[0][2].equals(field[2][0]) &&
                !field[0][2].isEmpty()) {
            return true;
        }

        return false;
    }

    private void showWinner(String winnerText) {
        statusText.setText(winnerText);
        Toast.makeText(this, winnerText, Toast.LENGTH_SHORT).show();

        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setEnabled(false);
            }
        }
    }

    private void resetGame() {
        roundCount = 0;
        playerXTurn = true;
        statusText.setText("Player X's Turn");

        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setText("");
                btn.setEnabled(true);
                btn.setTextColor(ContextCompat.getColor(this, android.R.color.black)); // reset color
            }
        }
    }
}

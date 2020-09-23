package com.tictactoe.tttf;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FIRST_PLAYER_WINNER_TEXT = "Winner : Player 1";
    private static final String SECOND_PLAYER_WINNER_TEXT = "Winner : Player 2";
    private static final String MATCH_DRAW_TEXT = "Match Draw";
    private static final String FIRST_PLAYER_TEXT = "X";
    private static final String SECOND_PLAYER_TEXT = "O";
    @ColorInt private static final int FIRST_PLAYER_TEXT_COLOR = 0XFF1F84D5;
    @ColorInt private static final int SECOND_PLAYER_TEXT_COLOR = 0XFFF11A39;

    private Button[][] grid = new Button[3][3];
    private AppCompatImageView passPhoneToPlayerOne;
    private AppCompatImageView passPhoneToPlayerTwo;
    private TextView playerOneScoreBoard;
    private TextView playerTwoScoreBoard;
    private String winnerText;
    @IntRange(from = 0) private int playerOnePoints;
    @IntRange(from = 0) private int playerTwoPoints;
    @IntRange(from = 0) private int totalCount;
    private boolean playerOneTurn;
    private boolean isDialogBoxOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        playerOneScoreBoard = findViewById(R.id.player_one_score_board);
        playerTwoScoreBoard = findViewById(R.id.player_two_score_board);
        passPhoneToPlayerOne = findViewById(R.id.pass_phone_to_player_one);
        passPhoneToPlayerTwo = findViewById(R.id.pass_phone_to_player_two);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String gridId = "grid_" + i + j;
                int resId = getResources().getIdentifier(gridId, "id", getPackageName());
                grid[i][j] = findViewById(resId);
                grid[i][j].setOnClickListener(this);
            }
        }

        if (savedInstanceState != null) {
            totalCount = savedInstanceState.getInt("totalCount");
            playerOnePoints = savedInstanceState.getInt("playerOnePoints");
            playerTwoPoints = savedInstanceState.getInt("playerTwoPoints");
            playerOneTurn = savedInstanceState.getBoolean("playerOneTurn");
            isDialogBoxOpen = savedInstanceState.getBoolean("isDialogBoxOpen");
            winnerText = savedInstanceState.getString("winnerText");

            grid[0][0].setText(savedInstanceState.getString("grid00Text"));
            grid[0][0].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid00Color"));
            grid[0][1].setText(savedInstanceState.getString("grid01Text"));
            grid[0][1].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid01Color"));
            grid[0][2].setText(savedInstanceState.getString("grid02Text"));
            grid[0][2].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid02Color"));
            grid[1][0].setText(savedInstanceState.getString("grid10Text"));
            grid[1][0].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid10Color"));
            grid[1][1].setText(savedInstanceState.getString("grid11Text"));
            grid[1][1].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid11Color"));
            grid[1][2].setText(savedInstanceState.getString("grid12Text"));
            grid[1][2].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid12Color"));
            grid[2][0].setText(savedInstanceState.getString("grid20Text"));
            grid[2][0].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid20Color"));
            grid[2][1].setText(savedInstanceState.getString("grid21Text"));
            grid[2][1].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid21Color"));
            grid[2][2].setText(savedInstanceState.getString("grid22Text"));
            grid[2][2].setTextColor((ColorStateList) savedInstanceState.getParcelable("grid22Color"));
        }

        if (isDialogBoxOpen) {
            showDialog();
        }
        passPhoneToPlayerOne.setVisibility(playerOneTurn ? View.VISIBLE : View.INVISIBLE);
        passPhoneToPlayerTwo.setVisibility(playerOneTurn ? View.INVISIBLE : View.VISIBLE);
        Button buttonReset = findViewById(R.id.reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("totalCount", totalCount);
        outState.putInt("playerOnePoints", playerOnePoints);
        outState.putInt("playerTwoPoints", playerTwoPoints);
        outState.putBoolean("playerOneTurn", playerOneTurn);
        outState.putBoolean("isDialogBoxOpen", isDialogBoxOpen);
        outState.putString("winnerText", winnerText);

        outState.putString("grid00Text", grid[0][0].getText().toString());
        outState.putParcelable("grid00Color", grid[0][0].getTextColors());
        outState.putString("grid01Text", grid[0][1].getText().toString());
        outState.putParcelable("grid01Color", grid[0][1].getTextColors());
        outState.putString("grid02Text", grid[0][2].getText().toString());
        outState.putParcelable("grid02Color", grid[0][2].getTextColors());
        outState.putString("grid10Text", grid[1][0].getText().toString());
        outState.putParcelable("grid10Color", grid[1][0].getTextColors());
        outState.putString("grid11Text", grid[1][1].getText().toString());
        outState.putParcelable("grid11Color", grid[1][1].getTextColors());
        outState.putString("grid12Text", grid[1][2].getText().toString());
        outState.putParcelable("grid12Color", grid[1][2].getTextColors());
        outState.putString("grid20Text", grid[2][0].getText().toString());
        outState.putParcelable("grid20Color", grid[2][0].getTextColors());
        outState.putString("grid21Text", grid[2][1].getText().toString());
        outState.putParcelable("grid21Color", grid[2][1].getTextColors());
        outState.putString("grid22Text", grid[2][2].getText().toString());
        outState.putParcelable("grid22Color", grid[2][2].getTextColors());
    }

    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        totalCount++;

        if (playerOneTurn) {
            ((Button) v).setText(FIRST_PLAYER_TEXT);
            ((Button) v).setTextColor(FIRST_PLAYER_TEXT_COLOR);
        } else {
            ((Button) v).setText(SECOND_PLAYER_TEXT);
            ((Button) v).setTextColor(SECOND_PLAYER_TEXT_COLOR);
        }

        if (checkForWin()) {
            if (playerOneTurn) {
                playerOneWins();
            } else {
                playerTwoWins();
            }
        } else if (totalCount == 9) {
            matchDraw();
        } else {
            switchPlayersTurn();
        }
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = grid[i][j].getText().toString();
            }
        }

        for (int i = 0; i < 3; i++) {
            if ((field[0][i].equals(field[1][i]) && field[0][i].equals(field[2][i]) && !field[0][i].equals(""))
                    || (field[i][0].equals(field[i][1]) && field[i][0].equals(field[i][2]) && !field[i][0].equals(""))) {
                return true;
            }
        }

        return (field[0][0].equals(field[1][1]) && field[0][0].equals(field[2][2]) && !field[0][0].equals(""))
                || (field[0][2].equals(field[1][1]) && field[0][2].equals(field[2][0]) && !field[0][2].equals(""));
    }

    private void updateScoreBoard() {
        playerOneScoreBoard.setText(MessageFormat.format("Player 1: {0}", playerOnePoints));
        playerTwoScoreBoard.setText(MessageFormat.format("Player 2: {0}", playerTwoPoints));
        resetTicTacToeBoard();
    }

    private void resetTicTacToeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j].setText("");
            }
        }
        totalCount = 0;
        switchPlayersTurn();
    }

    private void switchPlayersTurn() {
        playerOneTurn = !playerOneTurn;
        passPhoneToPlayerOne.setVisibility(playerOneTurn ? View.VISIBLE : View.INVISIBLE);
        passPhoneToPlayerTwo.setVisibility(playerOneTurn ? View.INVISIBLE : View.VISIBLE);
    }

    private void playerOneWins() {
        playerOnePoints++;
        winnerText = FIRST_PLAYER_WINNER_TEXT;
        showDialog();
    }

    private void playerTwoWins() {
        playerTwoPoints++;
        winnerText = SECOND_PLAYER_WINNER_TEXT;
        showDialog();
    }

    private void matchDraw() {
        winnerText = MATCH_DRAW_TEXT;
        showDialog();
    }

    private void resetGame() {
        playerOnePoints = 0;
        playerTwoPoints = 0;
        updateScoreBoard();
    }

    private void showDialog() {
        isDialogBoxOpen = true;
        new AlertDialog.Builder(this)
                .setTitle(winnerText)
                .setPositiveButton(R.string.alert_dialog_restart_game, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDialogBoxOpen = false;
                        resetGame();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDialogBoxOpen = false;
                        updateScoreBoard();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        isDialogBoxOpen = false;
                        updateScoreBoard();
                    }
                })
                .show();
    }
}

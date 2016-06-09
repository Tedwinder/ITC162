package com.example.christinac.tictactoegame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class TicTacToeActivity extends Activity
        implements OnClickListener {

    //define instance variables
    private boolean xTurn = false;
    private char board[][] = new char[3][3];
    private String messageString = "";
    private String gameString = "";


    //define variable for the widget
    private Button newGameButton;
    private TextView messageTextView;


    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);


        //get reference to the widget
        newGameButton = (Button) findViewById(R.id.newGameButton);
        messageTextView = (TextView) findViewById(R.id.messageTextView);

        //set the listener
        newGameButton.setOnClickListener(this);

        //get SharedPreference object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        resetButtons();
        setOnClickListener();


    }


    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();

        // create game string
        gameString = "";
        String square = "";
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                //square = board[x][y].getText().toString();
                if (square.equals("")) {
                    square = " "; // use a space for an empty square
                }
                gameString += square;
            }
        }

// save the instance variables



        editor.putString("messageString", messageString);
        editor.putBoolean("xTurn", xTurn);
        editor.putString("gameString", gameString);
        editor.commit();


        super.onPause();
    }


    @Override
    public void onResume() {
        super.onResume();


        xTurn = savedValues.getBoolean("xTurn", false);
        messageString = savedValues.getString("messageString", "");
        savedValues.getInt("x", 0);
        savedValues.getInt("o", 0);

        messageTextView.setText(messageString);

    }

    public void newGame(View view) {
        xTurn = false;
        board = new char[3][3];

        resetButtons();
    }

    private void resetButtons() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    if (R.getChildAt(x) instanceof Button) {
                        Button B = (Button) R.getChildAt(x);
                        B.setText("");
                        B.setEnabled(true);
                    }
                }
            }
        }
        messageTextView.setText("");
    }

    private boolean checkWin() {

        char winner = '\0';
        if (checkWinner(board, 3, 'X')) {
            winner = 'X';
        } else if (checkWinner(board, 3, 'O')) {
            winner = 'O';
        }else if(checkTie(board, 3)&!checkWinner(board, 3, 'X')){
            messageTextView.setText("It's a tie");
            return false;
        }

        if (winner == '\0') {
            return false; // nobody won
        } else {
            // display winner
            messageTextView.setText(winner + " wins!");
            return true;
        }
    }

    private boolean checkTie(char[][]board, int size){
        int total = 0;
        for(int x = 0; x<size; x++){
            for (int y = 0; y<size; y++){
                if(board[x][y]=='X'){
                    total++;
                }
            }
        }if (total==5){
            return true;
        }else
            return false;
    }


    private boolean checkWinner(char[][] board, int size, char player) {

        // check each column
        for (int x = 0; x < size; x++) {
            int total = 0;
            for (int y = 0; y < size; y++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true; // they win
            }
        }

        // check each row
        for (int y = 0; y < size; y++) {
            int total = 0;
            for (int x = 0; x < size; x++) {
                if (board[x][y] == player) {
                    total++;
                }
            }
            if (total >= size) {
                return true; // they win
            }
        }

        // forward diag
        int total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == y && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true; // they win
        }

        // backward diag
        total = 0;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x + y == size - 1 && board[x][y] == player) {
                    total++;
                }
            }
        }
        if (total >= size) {
            return true; // they win
        }
        return false; // nobody won
    }

    private void disableButtons() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    if (R.getChildAt(x) instanceof Button) {
                        Button B = (Button) R.getChildAt(x);
                        B.setEnabled(false);
                    }
                }
            }
        }
    }

    private void setOnClickListener() {
        TableLayout T = (TableLayout) findViewById(R.id.tableLayout);
        for (int y = 0; y < T.getChildCount(); y++) {
            if (T.getChildAt(y) instanceof TableRow) {
                TableRow R = (TableRow) T.getChildAt(y);
                for (int x = 0; x < R.getChildCount(); x++) {
                    View V = R.getChildAt(x);
                    V.setOnClickListener(new PlayOnClick(x, y));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.newGameButton) {
                newGame(v);
                return;

        }
    }

    private class PlayOnClick implements View.OnClickListener {

        private int x = 0;
        private int y = 0;

        public PlayOnClick(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View view) {
            if (view instanceof Button) {
                Button B = (Button) view;
                board[x][y] = xTurn ? 'O' : 'X';
                B.setText(xTurn ? "O" : "X");
                B.setEnabled(false);
                xTurn = !xTurn;

                // check if anyone has won
                if (checkWin()) {
                    disableButtons();
                }
            }
        }
    }


}

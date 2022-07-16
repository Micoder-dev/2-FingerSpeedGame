package com.example.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView, aThousandTextView;
    private Button tapTapButton;

    private CountDownTimer countDownTimer;

    private long initialCountDownInMillis = 60000;
    private int timerInterval = 1000;
    private int remainingTime = 60;

    private int aThousand = 10;

    private final String REMAINING_TIME_KEY = "remaining time key";
    private final String A_THOUSAND_KEY = "a thousand key";

    @Override
    protected void onDestroy() {
        super.onDestroy();

        showToast("on destroy method called");

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        showToast("on saveInstance called");

        outState.putInt(REMAINING_TIME_KEY, remainingTime);
        outState.putInt(A_THOUSAND_KEY, aThousand);
        countDownTimer.cancel();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToast("on create method called");

        timerTextView = findViewById(R.id.txtTimer);
        aThousandTextView = findViewById(R.id.txtAThousand);
        tapTapButton = findViewById(R.id.btnTap);

        aThousandTextView.setText(aThousand + "");

        if (savedInstanceState != null) {

            remainingTime = savedInstanceState.getInt(REMAINING_TIME_KEY);
            aThousand = savedInstanceState.getInt(A_THOUSAND_KEY);

            restoreTheGame();

        }

        tapTapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            //  aThousand = aThousand - 1;
                aThousand--;

                aThousandTextView.setText(aThousand + "");

                if (remainingTime > 0 && aThousand <= 0) {

                    Toast.makeText(MainActivity.this, "You Won", Toast.LENGTH_SHORT).show();

                    showAlert("Congratulations", "restart again?");

                }

            }
        });

        if (savedInstanceState == null) {
            countDownTimer = new CountDownTimer(initialCountDownInMillis, timerInterval) {
                @Override
                public void onTick(long millisUntilFinished) {

                    remainingTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(remainingTime + "");

                }

                @Override
                public void onFinish() {

                    Toast.makeText(MainActivity.this, "CountDown Finished", Toast.LENGTH_SHORT).show();

                    showAlert("Not Intresting", "would you like to try again?");

                }
            };

            countDownTimer.start();
        }

    }

    private void restoreTheGame() {

        int restoredRemainingTime = remainingTime;
        int restoredAThousand = aThousand;

        timerTextView.setText(restoredRemainingTime + "");
        aThousandTextView.setText(restoredAThousand + "");

        countDownTimer = new CountDownTimer((long) remainingTime * 1000, timerInterval) {
            @Override
            public void onTick(long millisUntilFinished) {

                remainingTime = (int) millisUntilFinished / 1000;
                timerTextView.setText(remainingTime + "");

            }

            @Override
            public void onFinish() {

                showAlert("Finished", "would you like to reset the game?");

            }
        };

        countDownTimer.start();

    }

    private void resetTheGame() {

        if (countDownTimer != null) {
            countDownTimer.cancel();

            countDownTimer = null;
        }

        aThousand = 10;
        aThousandTextView.setText(Integer.toString(aThousand));

        timerTextView.setText(remainingTime + "");

        countDownTimer = new CountDownTimer(initialCountDownInMillis, timerInterval) {
            @Override
            public void onTick(long millisToFinish) {

                remainingTime = (int) millisToFinish / 1000;
                timerTextView.setText(remainingTime + "");

            }

            @Override
            public void onFinish() {

                showAlert("Finished", "would you like to reset the game?");

            }
        };

        countDownTimer.start();

    }

    private void showAlert(String title, String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this)
                //set icon
                //.setIcon(android.R.drawable.ic_dialog_alert)
                //set title
                .setTitle(title)
                //set message
                .setMessage(message)
                //set positive button
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked

                        resetTheGame();

                    }
                })
                //set negative button
                /*.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                        Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                    }
                })*/
                .show();
                alertDialog.setCanceledOnTouchOutside(false);

    }

    private void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
package com.example.treasurehunt2k19;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scan_code_activity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);

        setContentView(ScannerView);
    }

    @Override
    public void handleResult(Result result) {
        if (result.getText().contains(":")) {
            String[] answer = result.getText().split(":");
            int ans = MainActivity.sharedPreferences.getInt(MainActivity.wrong_attempts,0);
            if (ans==3){
                Toast.makeText(getApplicationContext(), "You have used all your attempts", Toast.LENGTH_LONG).show();
                MainActivity.Result.setText("You have used all your attempts, Game Over!!");
                onBackPressed();

            }
            else {
                boolean check_data = MainActivity.db.check_data(answer[0].trim());
                if (check_data) {
                    MainActivity.Result.setText(answer[1]);
                    boolean inserted = MainActivity.db.insertData(answer[0].trim(), answer[1].trim());
                    if (inserted) {
                        Toast.makeText(getApplicationContext(), "Clue is Recorded", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Clue is not Recorded", Toast.LENGTH_LONG).show();
                    }
                    onBackPressed();
                }
                else {
                    Cursor cursor = MainActivity.db.getData();
                    if(cursor.getCount()==6){
                        MainActivity.Result.setText("You have Found all the clues");
                        Toast.makeText(getApplicationContext(), "You have Found all the clues", Toast.LENGTH_LONG).show();
                        onBackPressed();
                    }
                    else {
                        if (ans == 2){
                            MainActivity.db.reset_game();
                            MainActivity.Result.setText("You have used all your attempts, Game Over");
                           Toast.makeText(getApplicationContext(), "You have used all your attempts", Toast.LENGTH_LONG).show();

                        }
                        else {
                            MainActivity.Result.setText("This is not the Correct clue");
                            Toast.makeText(getApplicationContext(), "This is not the Correct clue", Toast.LENGTH_LONG).show();
                        }
                            SharedPreferences.Editor editor = MainActivity.sharedPreferences.edit();
                            editor.putInt(MainActivity.wrong_attempts, ans + 1);
                            editor.apply();

                        onBackPressed();
                    }
                }
            }
        }
        else {
            MainActivity.Result.setText("This QR is not a Part of Treasue Hunt");
            Toast.makeText(getApplicationContext(), "This QR is not a Part of Treasue Hunt", Toast.LENGTH_LONG).show();
            onBackPressed();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();

    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

}

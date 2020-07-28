package com.example.treasurehunt2k19;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper db;

     public static TextView Result;
     public static final String sharedpref = "Shared";
    public static final String wrong_attempts = "wrong_attempts";
    public static SharedPreferences sharedPreferences;

     Button scan_code,show_data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sharedPreferences = getSharedPreferences(sharedpref,MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);

        Result = findViewById(R.id.result);
        scan_code = findViewById(R.id.scan_code);
        show_data = findViewById(R.id.show_data);


        scan_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getApplicationContext(),scan_code_activity.class));

                } else {
                    verify_permission();
                }

            }
        });

        show_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = db.getData();
                StringBuffer buffer = new StringBuffer();

                int ans = sharedPreferences.getInt(wrong_attempts,0);


                if(res.getCount() == 0){
                    buffer.append("No Clue Found");
                    showMessage("Wrong Attempts "+Integer.toString(ans),buffer.toString());
                    return;
                }


                int i =1;
                while(res.moveToNext()){
                    buffer.append("Clue "+ Integer.toString(i)+" :"+res.getString(1)+"\n");
                    buffer.append("Time : "+res.getString(2)+"\n\n");
                    i++;
                }
                showMessage("Wrong Attempts "+Integer.toString(ans),buffer.toString());

            }
        });

    }

    public void showMessage(String correct,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.alert_layout,null);
        builder.setView(mview);
        TextView heading = mview.findViewById(R.id.Heading);
        TextView correct_attempts = mview.findViewById(R.id.correct_attempts);
        TextView data = mview.findViewById(R.id.data);

        heading.setText("Hints");
        correct_attempts.setText(correct);
        data.setText(message);
        builder.setCancelable(true);

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.60);


        builder.show().getWindow().setLayout(width, height);
//       builder.show();

    }

    private void verify_permission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed for Accessing Camera")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[] {Manifest.permission.CAMERA}, 1);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CAMERA}, 1);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] getResults){
        //verify_permission();
    }
}

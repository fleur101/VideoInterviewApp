package com.mirka.app.naimi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY_TAG";
    private TextView mWelcomeMessageTextView;
    private Button mInterviewButton;
    private EditText mNameEditText;
    public static String mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // comment commit
        mWelcomeMessageTextView = (TextView) findViewById(R.id.tv_welcome_message);
        mInterviewButton = (Button) findViewById(R.id.btn_start_interview);
        mNameEditText = (EditText) findViewById(R.id.et_person_name);

    }

    public static String getName(){
        return mName;
    }
    public void onInterviewButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        mName = mNameEditText.getText().toString();
        startActivity(intent);
    }
}

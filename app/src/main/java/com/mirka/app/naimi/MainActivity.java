package com.mirka.app.naimi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mWelcomeMessageTextView;
    private Button mInterviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // comment commit
        mWelcomeMessageTextView = (TextView) findViewById(R.id.tv_welcome_message);
        mInterviewButton = (Button) findViewById(R.id.btn_start_interview);

        mWelcomeMessageTextView.setText(R.string.welcome_message);
    }

    public void onInterviewButtonClick(View view) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
    }
}

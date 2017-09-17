package com.mirka.app.naimi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

        Log.e(TAG, "onCreate: mName"+mName );
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

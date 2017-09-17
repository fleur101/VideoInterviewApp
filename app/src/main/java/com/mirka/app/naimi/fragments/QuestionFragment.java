package com.mirka.app.naimi.fragments;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirka.app.naimi.R;
import com.mirka.app.naimi.TestActivity;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QUESTION_FRAGMENT_TAG";
    private TextView mTimerTextView;
    private TextView mFinishInterviewTextView;
    private RelativeLayout mQuestionRelativeLayout;
    public static final int QUESTION_TIME = 10 * 1000;
    TestActivity parentActivity;
    CountDownTimer timer;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_question, container, false);
        Log.e(TAG, "onCreateView: got into question fragment");

        mQuestionRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_question);
        mFinishInterviewTextView = (TextView) view.findViewById(R.id.tv_end_interview);
        TextView mQuestionTextView = (TextView) view.findViewById(R.id.tv_question_text);
        mTimerTextView = (TextView) view.findViewById(R.id.tv_timer_text);
        Button mSkipQuestionButton = (Button) view.findViewById(R.id.btn_question_fragment);
        parentActivity = ((TestActivity)getActivity());
        if (TestActivity.getQuestionNum() == 5) {
            mQuestionRelativeLayout.setVisibility(View.GONE);
            mFinishInterviewTextView.setVisibility(View.VISIBLE);
        } else {
            mQuestionTextView.setText(TestActivity.mQuestionList.get(TestActivity.getQuestionNum()));
            TestActivity.increaseQuestionNum();
            timer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    timer.cancel();
                    //parentActivity.startVideo();
                    startVideo();
                }
            }.start();

            mSkipQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                    // ((TestActivity)getActivity()).startVideo();
                    startVideo();
                }
            });

        }
        return view;
    }
    public void startVideo(){
        Log.e(TAG, "startVideo: started video");
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.frame_camera, cameraFragment, CameraFragment.TAG);
       // transaction.replace(id, fragment, tag);
        transaction.replace(R.id.fragment_container, new CameraFragment(), CameraFragment.TAG);
        transaction.commit();
       // replaceFragment(R.id.fragment_container, new CameraFragment(), CameraFragment.TAG);
    }




}

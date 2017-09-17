package com.mirka.app.naimi.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirka.app.naimi.MainActivity;
import com.mirka.app.naimi.R;
import com.mirka.app.naimi.TestActivity;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QUESTION_FRAGMENT_TAG";
    private TextView mTimerTextView;
    private TextView mFinishInterviewTextView;
    private TextView mNameTextView;
    private RelativeLayout mQuestionRelativeLayout;
    private String questionName;
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
        mNameTextView = (TextView) view.findViewById(R.id.tv_my_name);
        mQuestionRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_question);
        mFinishInterviewTextView = (TextView) view.findViewById(R.id.tv_end_interview);
        TextView mQuestionTextView = (TextView) view.findViewById(R.id.tv_question_text);
        mTimerTextView = (TextView) view.findViewById(R.id.tv_timer_text);
        Button mSkipQuestionButton = (Button) view.findViewById(R.id.btn_question_fragment);
        parentActivity = ((TestActivity)getActivity());
        questionName = TestActivity.mQuestionList.get(TestActivity.getQuestionNum());
        mQuestionTextView.setText(questionName);
        Log.e(TAG, "onCreateView: quetsion num"+TestActivity.getQuestionNum());
        mNameTextView.setText(MainActivity.getName());
        if (TestActivity.getQuestionNum() == 4) {
            Log.e(TAG, "onCreateView: question num is 4");
            mQuestionRelativeLayout.setVisibility(View.GONE);
            mFinishInterviewTextView.setVisibility(View.VISIBLE);
            mNameTextView.setVisibility(View.VISIBLE);
        } else {
            TestActivity.increaseQuestionNum();
            Log.e(TAG, "onCreateView: now question num"+questionName);
            timer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimerTextView.setText(String.valueOf(millisUntilFinished / 1000));
                }

                @Override
                public void onFinish() {
                    timer.cancel();
                    startVideo();
                }
            }.start();

            mSkipQuestionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timer.cancel();
                    startVideo();
                }
            });

        }
        return view;
    }
    public void startVideo(){
        Log.e(TAG, "startVideo: started video");
        Bundle bundle = new Bundle();
        bundle.putString("question", questionName);
        Fragment fragment = new CameraFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, CameraFragment.TAG);
       // transaction.addToBackStack(null);
        transaction.commit();
    }




}

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
import android.widget.Toast;

import com.mirka.app.naimi.R;
import com.mirka.app.naimi.TestActivity;
import com.mirka.app.naimi.data.AppData;
import com.mirka.app.naimi.utils.VideoEditingUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.mirka.app.naimi.data.AppData.base_filename;
import static com.mirka.app.naimi.data.AppData.base_filename_subs;
import static com.mirka.app.naimi.data.AppData.getNumberOfQuestions;

public class QuestionFragment extends Fragment {

    public static final String TAG = "QUESTION_FRAGMENT_TAG";
    private TextView mTimerTextView;
    private TextView mFinishInterviewTextView;
    private RelativeLayout mQuestionRelativeLayout;
    private TextView mNameTextView;
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
        Context context = getActivity().getApplicationContext();
        if (TestActivity.getQuestionNum() == getNumberOfQuestions()) {
            mQuestionRelativeLayout.setVisibility(View.GONE);
            mFinishInterviewTextView.setVisibility(View.VISIBLE);
            mNameTextView.setVisibility(View.VISIBLE);
            String[] names = new String[getNumberOfQuestions()];
            for (int i = 0; i<getNumberOfQuestions(); i++){
                names[i]=base_filename+(i+1)+".mp4";
            }

            List<Long> timings = VideoEditingUtils.getSubtitlesTiming(context, names);
            VideoEditingUtils.buildSRTFile(AppData.getQuestions(), timings);
            try {
                String withoutSubsPath = VideoEditingUtils.appendVideo(names, getActivity().getApplicationContext());
                Toast.makeText(parentActivity, withoutSubsPath, Toast.LENGTH_SHORT).show();
                VideoEditingUtils.embedSubtitlesToVideo(getActivity().getApplicationContext(), withoutSubsPath, base_filename_subs);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(parentActivity, "Error appending" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            mQuestionTextView.setText(TestActivity.mQuestionList.get(TestActivity.getQuestionNum()));
            TestActivity.increaseQuestionNum();
            timer = new CountDownTimer(QUESTION_TIME, 1000) {
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
        Bundle bundle = new Bundle();
         bundle.putString("question", questionName);
        Fragment fragment = new CameraFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.frame_camera, cameraFragment, CameraFragment.TAG);
       // transaction.replace(id, fragment, tag);
        transaction.replace(R.id.fragment_container, fragment, CameraFragment.TAG);
        transaction.commit();
       // replaceFragment(R.id.fragment_container, new CameraFragment(), CameraFragment.TAG);
    }




}

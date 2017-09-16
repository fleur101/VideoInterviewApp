package com.mirka.app.naimi.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirka.app.naimi.R;

import java.util.ArrayList;

public class QuestionFragment extends Fragment {

    private TextView mQuestionTextView;
    private ArrayList<String> mQuestionList;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        // filling questions list
        mQuestionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) mQuestionList.add("Question number " + i);

        mQuestionTextView = (TextView) view.findViewById(R.id.tv_question_text);
        mQuestionTextView.setText(mQuestionList.get(0));

        return view;
    }

}

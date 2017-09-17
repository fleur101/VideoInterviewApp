package com.mirka.app.naimi.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mirka.app.naimi.R;
import com.mirka.app.naimi.TestActivity;
import com.mirka.app.naimi.utils.CameraPreview;

import java.io.File;
import java.io.IOException;

public class CameraFragment extends Fragment {

    public static final String TAG = "CAMERA_FRAGMENT_TAG";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 7;

    public static final int PREPARATION_TIME = 4 * 1000;
    public static final int RECORDING_TIME = 2 * 60 * 1000;

    private int frontCameraId;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private TextView mPreviewTimerTextView;
    private TextView mRecordTimerTextView;
    private TextView mQuestionSubtitleTextView;
    private RelativeLayout mRecordRelativeLayout;
    private MediaRecorder mMediaRecorder;
    private boolean isRecording = false;
    private String question;

    TestActivity parentActivity;
    public static CountDownTimer timer;
    public CameraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle!=null) question = bundle.getString("question", question);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        frontCameraId = TestActivity.getFrontCameraId(getActivity());
        // inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);
        Log.e(TAG, "onCreateView: got into camera fragment");
        preview = (FrameLayout) view.findViewById(R.id.camera_preview);
        mRecordRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_camera_record);
        mPreviewTimerTextView = (TextView)view.findViewById(R.id.tv_preview_timer);
        mRecordTimerTextView = (TextView)view.findViewById(R.id.tv_record_timer);
        mQuestionSubtitleTextView = (TextView) view.findViewById(R.id.tv_question_subtitle);
        mQuestionSubtitleTextView.setText(question);
        final Button mStopRecordingButton = (Button) view.findViewById(R.id.btn_camera_fragment);
        launchCameraPreview();
        parentActivity = ((TestActivity)getActivity());
        timer = new CountDownTimer(PREPARATION_TIME, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mPreviewTimerTextView.setText(String.valueOf(millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                Log.e(TAG, "onFinish: finished preview");
                recordVideo("interview");
                mRecordRelativeLayout.setVisibility(View.VISIBLE);
                timer = new CountDownTimer(RECORDING_TIME, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mPreviewTimerTextView.setVisibility(View.GONE);
                        mRecordTimerTextView.setText(String.format("%02d:%02d", (millisUntilFinished/1000) / 60, (millisUntilFinished/1000) % 60));
                        mQuestionSubtitleTextView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        Log.e(TAG, "onFinish: finished recording");
                        recordVideo("interview");
                    }
                }.start();
            }
        }.start();

        mStopRecordingButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                timer.cancel();
                startQuestion();
            }
        });
        return view;
    }
    public void startQuestion(){
        Log.e(TAG, "startQuestion: started question fragment");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
      //  transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, new QuestionFragment(), QuestionFragment.TAG);
        transaction.commit();

    }
    private void launchCameraPreview() {
        mCamera = getCameraInstance(getActivity(), frontCameraId);
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview.addView(mPreview);
    }



    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(Context context, int id){
        Camera c = null;
        try {
            c = Camera.open(id); // attempt to get a Camera instance
        } catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return c; // returns null if camera is unavailable
    }



    @Override
    public void onPause() {
        super.onPause();
        // mPreview.getHolder().removeCallback(mPreview);
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    public void onResume() {
        super.onResume();
        // mButton.setEnabled(false);
        // parentActivity.ensurePermission();
    }


    private boolean prepareVideoRecorder(String videoname) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setOrientationHint(270);
        if (mCamera != null)mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));

        mMediaRecorder.setOutputFile(getOutputMediaFile(videoname).toString());
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.i("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.i("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }



    private void recordVideo(String videoname){
        if (isRecording) {
            // stop recording and release camera
            Log.e(TAG, "recordVideo: it is recording need to stop");
            mMediaRecorder.stop();  // stop the recording
            Toast.makeText(getActivity(), "video stopped", Toast.LENGTH_SHORT).show();
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder
            isRecording = false;
            startQuestion();
        } else {
            // initialize video camera
            if (prepareVideoRecorder(videoname)) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                Log.e(TAG, "recordVideo: not recording, therefore start recording");
                mMediaRecorder.start();
                isRecording = true;
                Toast.makeText(getActivity(), "Video started", Toast.LENGTH_SHORT).show();
            } else {
                // prepare didn't work, release the camera
                Log.e(TAG, "recordVideo: prepare did not work");
                releaseMediaRecorder();
                // inform user
                Toast.makeText(getActivity(), "Camera does not work", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Create a file Uri for saving an image or video
    private static Uri getOutputMediaFileUri(String videoname){
        return Uri.fromFile(getOutputMediaFile(videoname));
    }

    // Create a File for saving an image or video
    private static File getOutputMediaFile(String videoname){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + videoname + ".mp4");
        return mediaFile;
    }



}

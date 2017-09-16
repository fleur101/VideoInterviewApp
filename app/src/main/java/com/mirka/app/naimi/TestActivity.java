package com.mirka.app.naimi;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.nfc.Tag;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mirka.app.naimi.fragments.CameraFragment;
import com.mirka.app.naimi.fragments.QuestionFragment;

import java.util.ArrayList;

import static com.mirka.app.naimi.fragments.CameraFragment.MY_PERMISSIONS_REQUEST_CAMERA;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TEST_ACTIVITY_TAG";
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private int frontCameraId;

    public CameraFragment cameraFragment;
    public QuestionFragment questionFragment;
    public static ArrayList<String> mQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        cameraFragment = new CameraFragment();
        questionFragment = new QuestionFragment();

        ensurePermission();
        fillQuestions();
    }

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            return true;
        } else {
            return false;
        }
    }

    /** Check if this device has a camera */
    public static int getFrontCameraId(Context context) {
        int cameraID = -1;
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.CameraInfo newInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(i, newInfo);
            if (newInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraID = i;
                break;
            }
        }
        return cameraID;
    }

    public void ensurePermission() {
        if (checkCameraHardware(this)  && (frontCameraId = getFrontCameraId(this)) != -1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                // show an explanation
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "You asked not to ask", Toast.LENGTH_LONG).show();
                } else {
                    // no explanation
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            } else {
                launchFragment();
            }
        } else {
            Toast.makeText(this, "No front camera", Toast.LENGTH_LONG).show();
        }
    }

    private void fillQuestions() {
        // filling questions list
        mQuestionList = new ArrayList<>();
        for (int i = 0; i < 5; i++) mQuestionList.add("Question number " + i);
    }

    public void launchFragment() {
        Toast.makeText(this, "HEY THERE", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add(R.id.frame_camera, cameraFragment, CameraFragment.TAG);
        transaction.add(R.id.fragment_container, questionFragment, QuestionFragment.TAG);
        transaction.commit();
    }

//    public void replaceFragment(int id, Fragment fragment, String tag) {
//        Toast.makeText(this, "HEY THERE", Toast.LENGTH_LONG).show();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
////        transaction.add(R.id.frame_camera, cameraFragment, CameraFragment.TAG);
//        transaction.replace(id, fragment, tag);
//        transaction.commit();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchFragment();
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }






}

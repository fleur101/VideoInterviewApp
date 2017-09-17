package com.mirka.app.naimi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mirka.app.naimi.fragments.CameraFragment;
import com.mirka.app.naimi.fragments.QuestionFragment;

import java.util.ArrayList;

import static com.mirka.app.naimi.fragments.CameraFragment.MY_PERMISSIONS_REQUEST_CAMERA;
import static com.mirka.app.naimi.fragments.CameraFragment.timer;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TEST_ACTIVITY_TAG";
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private int frontCameraId;
    public static int questionNum=0;

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

    public static void increaseQuestionNum(){
        questionNum++;
    }

    public static int getQuestionNum(){
        return questionNum;
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
        mQuestionList.add("Расскажите вкратце о себе");
        mQuestionList.add("Расскажите о своих профессиональных навыках");
        mQuestionList.add("Каков ваш опыт работы в данной сфере?");
        mQuestionList.add("Каковые ваши недостатки и достоинства?");
        mQuestionList.add("Почему Вы выбрали эту площадку?");
    }

    public void launchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, questionFragment, QuestionFragment.TAG);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (timer!=null){
            Log.e(TAG, "onBackPressed: timer is alive");
            timer.cancel();
            timer=null;
        }
//        final Fragment fragment = getFragmentManager().findFragmentById(R.id.fragment_container);
//        if (fragment!=null){
//            getFragmentManager().beginTransaction().remove(fragment).commit();
//        }

        super.onBackPressed();


    }

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

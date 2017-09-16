package com.mirka.app.naimi;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mirka.app.naimi.fragments.CameraFragment;
import com.mirka.app.naimi.fragments.QuestionFragment;

import static com.mirka.app.naimi.fragments.CameraFragment.MY_PERMISSIONS_REQUEST_CAMERA;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TEST_ACTIVITY_TAG";
    private int frontCameraId;
    CameraFragment cameraFragment;
    QuestionFragment questionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        cameraFragment = new CameraFragment();
        questionFragment = new QuestionFragment();



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
            }
        } else {
            Toast.makeText(this, "No front camera", Toast.LENGTH_LONG).show();
            System.exit(-1);
        }


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
    private int getFrontCameraId(Context context) {
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.frame_camera, cameraFragment, CameraFragment.TAG);
                transaction.add(R.id.frame_question, questionFragment, QuestionFragment.TAG);
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




}

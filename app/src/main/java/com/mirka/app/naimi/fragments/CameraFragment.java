package com.mirka.app.naimi.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mirka.app.naimi.R;
import com.mirka.app.naimi.TestActivity;
import com.mirka.app.naimi.utils.CameraPreview;

public class CameraFragment extends Fragment {

    public static final String TAG = "CAMERA_FRAGMENT_TAG";
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 7;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    private int frontCameraId;
    public CameraFragment() {
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
        View view = inflater.inflate(R.layout.fragment_camera, container, false);
        preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        launchCameraPreview();
        return view;
    }



    private void launchCameraPreview() {
        mCamera = getCameraInstance(getActivity(), frontCameraId);
        mPreview = new CameraPreview(getActivity(), mCamera);
        preview.addView(mPreview);
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


}

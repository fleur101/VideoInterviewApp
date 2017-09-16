package com.mirka.app.naimi.fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private static final String TAG = "CameraFragmentTag";
    private Camera mCamera;
    private CameraPreview mPreview;

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

        FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        if (checkCameraHardware(getContext())  && (frontCameraId = getFrontCameraId(getContext())) != -1) {
            mCamera = getCameraInstance(getContext(), frontCameraId);
            mPreview = new CameraPreview(getContext(), mCamera);
            preview.addView(mPreview);
        } else {
            Toast.makeText(getContext(), "Camera is not supported", Toast.LENGTH_SHORT).show();
        }

        return view;
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
        Log.e(TAG, "getCameraInstance()");
        try {
            // we suppose it

            c = Camera.open(id); // attempt to get a Camera instance
            Log.e(TAG, "getCameraInstance() try");
        } catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.e(TAG, "getCameraInstance() catch");
        }
        return c; // returns null if camera is unavailable
    }

}

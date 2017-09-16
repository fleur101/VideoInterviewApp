package com.mirka.app.naimi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.mirka.app.naimi.utils.CameraPreview;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class VideoTestActivity extends AppCompatActivity {

//    static final int REQUEST_VIDEO_CAPTURE = 1;
//    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
//    private static final String TAG = "VideoTestActivity";
//
//    private int frontCameraId;
//    private VideoView mVideoView;
//    private Camera mCamera;
//    private CameraPreview mPreview;
//    private MediaRecorder mMediaRecorder;
//    private boolean isRecording = false;
//    private Button mButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_test);
//
//        mVideoView = (VideoView) findViewById(R.id.vv_show_video);
//        mButton = (Button) findViewById(R.id.button);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mPreview.getHolder().removeCallback(mPreview);
//        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
//        releaseCamera();              // release the camera immediately on pause event
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mButton.setEnabled(false);
//        PermitCamera();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == MY_PERMISSIONS_REQUEST_CAMERA) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
//                // Create our Preview view and set it as the content of our activity.
//                setPreview();
//            } else {
//                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//    private void PermitCamera() {
//        if (checkCameraHardware(this) && (frontCameraId = getFrontCameraId(this)) != -1){
//            // Async permission test
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_DENIED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.CAMERA)) {
//                } else {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CAMERA},
//                            MY_PERMISSIONS_REQUEST_CAMERA);
//                }
//            } else {
//                setPreview();
//            }
//        } else {
//            Toast.makeText(this, "No front camera, sorry=(", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void setPreview() {
//        mCamera = getCameraInstance(this, frontCameraId);
//        if (mCamera == null) {
//            Toast.makeText(this, "Camera not found", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mPreview = new CameraPreview(this, mCamera);
//        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
//        preview.addView(mPreview);
//        mButton.setEnabled(true);
//    }
//
//    private boolean prepareVideoRecorder(String videoname) {
//
//        mMediaRecorder = new MediaRecorder();
//
//        mCamera.unlock();
//        mMediaRecorder.setCamera(mCamera);
//
//        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
//        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//
//        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
//
//        mMediaRecorder.setOutputFile(getOutputMediaFile(videoname).toString());
//        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
//        try {
//            mMediaRecorder.prepare();
//        } catch (IllegalStateException e) {
//            Log.i("TAG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
//            releaseMediaRecorder();
//            return false;
//        } catch (IOException e) {
//            Log.i("TAG", "IOException preparing MediaRecorder: " + e.getMessage());
//            releaseMediaRecorder();
//            return false;
//        }
//        return true;
//    }
//
//    private void releaseMediaRecorder(){
//        if (mMediaRecorder != null) {
//            mMediaRecorder.reset();   // clear recorder configuration
//            mMediaRecorder.release(); // release the recorder object
//            mMediaRecorder = null;
//            mCamera.lock();           // lock camera for later use
//        }
//    }
//
//    private void releaseCamera(){
//        if (mCamera != null){
//            mCamera.release();        // release the camera for other applications
//            mCamera = null;
//        }
//    }
//
//    public void buttonClickHandler(View view){
//        recordVideo("gohard");
//    }
//
//    private void recordVideo(String videoname){
//        if (isRecording) {
//            // stop recording and release camera
//            mMediaRecorder.stop();  // stop the recording
//            Toast.makeText(this, "video stopped", Toast.LENGTH_SHORT).show();
//            releaseMediaRecorder(); // release the MediaRecorder object
//            mCamera.lock();         // take camera access back from MediaRecorder
//
//            isRecording = false;
//
//        } else {
//            // initialize video camera
//            if (prepareVideoRecorder(videoname)) {
//                // Camera is available and unlocked, MediaRecorder is prepared,
//                // now you can start recording
//                mMediaRecorder.start();
//                isRecording = true;
//                Toast.makeText(this, "Video started", Toast.LENGTH_SHORT).show();
//            } else {
//                // prepare didn't work, release the camera
//                releaseMediaRecorder();
//                // inform user
//                Toast.makeText(this, "Camera does not work", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /** Create a file Uri for saving an image or video */
//    private static Uri getOutputMediaFileUri(String videoname){
//        return Uri.fromFile(getOutputMediaFile(videoname));
//    }
//
//    /** Create a File for saving an image or video */
//    private static File getOutputMediaFile(String videoname){
//        // To be safe, you should check that the SDCard is mounted
//        // using Environment.getExternalStorageState() before doing this.
//
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
//        // This location works best if you want the created images to be shared
//        // between applications and persist after your app has been uninstalled.
//
//        // Create the storage directory if it does not exist
//        if (! mediaStorageDir.exists()){
//            if (! mediaStorageDir.mkdirs()){
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//
//        // Create a media file name
//        File mediaFile;
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
//                    "VID_" + videoname + ".mp4");
//        return mediaFile;
//    }
//
//
//    /** A safe way to get an instance of the Camera object. */
//    public static Camera getCameraInstance(Context context, int id){
//        Camera c = null;
//        try {
//            c = Camera.open(id); // attempt to get a Camera instance
//        }
//        catch (Exception e){
//            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            // Camera is not available (in use or does not exist)
//        }
//        return c; // returns null if camera is unavailable
//    }
//
//    /** Check if this device has a camera */
//    private boolean checkCameraHardware(Context context) {
//        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
//            // this device has a camera
//            return true;
//        } else {
//            // no camera on this device
//            return false;
//        }
//    }
//
//    /** Check if this device has a camera */
//    private int getFrontCameraId(Context context) {
//        int cameraID = -1;
//        for (int i=0; i< Camera.getNumberOfCameras(); i++) {
//            Camera.CameraInfo newInfo = new Camera.CameraInfo();
//            Camera.getCameraInfo(i, newInfo);
//            if (newInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//                cameraID = i;
//                break;
//            }
//        }
//        return cameraID;
//    }
//
//    public void dispatchTakeVideoEvent(View view) {
//        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
//            Uri videoUri = intent.getData();
//            mVideoView.setVideoURI(videoUri);
//        }
//    }
}

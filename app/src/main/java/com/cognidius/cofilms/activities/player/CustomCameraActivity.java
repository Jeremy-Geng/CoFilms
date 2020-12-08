package com.cognidius.cofilms.activities.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.database.LoggedUser;
import com.cognidius.cofilms.database.room.Video;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class CustomCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CustomCameraActivity.class.getSimpleName();
    private boolean isQuestion = true;
    private String questionVideoId;
    private MediaRecorder mMediaRecorder;
    private TextureView mTextureView;
    private Button bStart, bStop;
    private android.hardware.Camera mCamera;
    private android.hardware.Camera.Size mSelectSize;
    private boolean isRecorder = false;
    private String videoFile;
    private ImageView cameraSwitch;
    private boolean faceCamera = false;
    private ImageView backtoUserMenu;
    private Video currentVideo;

    //auto focus
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 0x01:
//                    mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() { //auto focus
//                        @Override
//                        public void onAutoFocus(boolean success, android.hardware.Camera camera) {
//
//                        }
//                    });
//                    mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);// per 2s, auto focus repeatedly
//                    break;
//                default:
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_domain);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mTextureView = findViewById(R.id.textureView);
        bStart = findViewById(R.id.buttonStart);
        bStop = findViewById(R.id.buttonStop);
        cameraSwitch = findViewById(R.id.cameraSwitch);
        bStart.setOnClickListener(this);
        bStop.setOnClickListener(this);
        backtoUserMenu = findViewById(R.id.backToUserMenu);

        backtoUserMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomCameraActivity.this, UserMenuActivity.class);
                startActivity(intent);
            }
        });

        initTextureViewListener(faceCamera);

        cameraSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCamera.stopPreview();
                mCamera.release();
                faceCamera = !faceCamera;
                initCamera();
                initCameraConfig();
                try {
                    mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
                    mCamera.startPreview();
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            camera.cancelAutoFocus();
                        }
                    });
                    // mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initMediaRecorder();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            questionVideoId = extras.getString("parentId");
            String vt = extras.getString("videoType");
            isQuestion = !vt.equals("answer");
        }
    }

    private void initTextureViewListener(final boolean isFacing) {

        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                initCamera();

                if (mCamera == null) {
                    Log.e(TAG, "Set camera fail");
                } else {
                    Log.e(TAG, "Set camera success");

                }

                initCameraConfig();

                try {
                    mCamera.setPreviewTexture(surfaceTexture);
                    mCamera.startPreview();
                    //mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            camera.cancelAutoFocus();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                mCamera.stopPreview();
                mCamera.release();
                return true;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    private Integer selectCamera() {
        int cameraCount = android.hardware.Camera.getNumberOfCameras();
//        CameraInfo.CAMERA_FACING_BACK
//        CameraInfo.CAMERA_FACING_FRONT
        int facing = faceCamera ? android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT : android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
        Log.e(TAG, "selectCamera: cameraCount=" + cameraCount);
        if (cameraCount == 0) {
            Log.e(TAG, "selectCamera: The device does not have a camera ");
            return null;
        }
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            android.hardware.Camera.getCameraInfo(i, info);
            if (info.facing == facing) {
                return i;
            }

        }
        return null;

    }

    private void initCamera() {
        mCamera = android.hardware.Camera.open(selectCamera());
        mSelectSize = selectPreviewSize(mCamera.getParameters());
    }

    private void initCameraConfig() {
        android.hardware.Camera.Parameters parameters = mCamera.getParameters();
        if (!faceCamera) {
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);// Turn off the flash
            parameters.setFocusMode(android.hardware.Camera.Parameters.FLASH_MODE_AUTO); // Focus is set to automatic
        }
        parameters.setPreviewSize(mSelectSize.width, mSelectSize.height);// Set preview size
        parameters.setPictureSize(mSelectSize.width, mSelectSize.height);// Set image size
        parameters.set("orientation", "portrait");// Set photo orientation
        parameters.set("rotation", 90); // Rotate the photo lens angle by 90 degrees
        mCamera.setParameters(parameters);// Add parameters
        mCamera.setDisplayOrientation(90);// Set display direction
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
    }

    private void configMedioRecorder() {
        int rotation = 0;
        long timeStamp = System.currentTimeMillis();
        String thisVideoId = Long.toString(timeStamp);
        String videoType = "question";
        if(isQuestion){
            //videoFile = new File(getExternalCacheDir(),"/" + thisVideoId + "/Question.mp4");
            String path = getExternalCacheDir().getAbsolutePath() + "/" + thisVideoId+ "/";
            File dir = new File(path);
            if(!dir.exists()) dir.mkdirs();
            videoFile = path +  "Question.mp4";
            //videoFile = new File(file, thisVideoId+".mp4");
            currentVideo =  new Video(thisVideoId, LoggedUser.getUSERNAME(), videoType);
        }else{
            //videoFile = new File(getExternalCacheDir(), "/" + questionVideoId + "/" + thisVideoId + ".mp4"  );
            //videoFile = new File(getExternalCacheDir(), thisVideoId+".mp4");
            String path = getExternalCacheDir().getAbsolutePath() + "/" + questionVideoId + "/";
            File dir = new File(path);
            if(!dir.exists()) dir.mkdirs();
            videoFile = path + thisVideoId + ".mp4";
            videoType = "answer";
            currentVideo =  new Video(thisVideoId, LoggedUser.getUSERNAME(), videoType);
            currentVideo.setParentId(questionVideoId);
        }

        VideoInfoCollectionActivity.setCurrentVideo(currentVideo);

        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);// Set audio source
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);// Set video source
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);// Set audio output format
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);// Set audio encoding format
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);// Set video encoding format

        // comment for some devices
        //mMediaRecorder.setVideoSize(mSelectSize.width, mSelectSize.height);// Set video resolution
        mMediaRecorder.setVideoEncodingBitRate(8 * 1920 * 1080);// Set the bit rate of the video

        //comment for some devices
        //mMediaRecorder.setVideoFrameRate(30);// Set the frame rate of the video
        rotation = faceCamera ? 270 : 90;
        mMediaRecorder.setOrientationHint(rotation);// Set the angle of the video
        mMediaRecorder.setMaxDuration(60 * 1000);// Set maximum recording time
        Surface surface = new Surface(mTextureView.getSurfaceTexture());
        mMediaRecorder.setPreviewDisplay(surface);// Set preview
        mMediaRecorder.setOutputFile(videoFile);// Set file save path
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() { //录制异常监听
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                try {
                    mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
                    mCamera.startPreview();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private android.hardware.Camera.Size selectPreviewSize(android.hardware.Camera.Parameters parameters) {
        List<android.hardware.Camera.Size> previewSizeList = parameters.getSupportedPreviewSizes();
        if (previewSizeList.size() == 0) {
            Log.e(TAG, "selectPreviewSize: previewSizeList size is 0");
            return null;

        }

        android.hardware.Camera.Size currentSelectSize = null;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        for (int i = 1; i < 41; i++) {
            for (android.hardware.Camera.Size itemSize : previewSizeList) {
                if (itemSize.height > (deviceWidth - i * 5) && itemSize.height < (deviceWidth + i * 5)) {
                    if (currentSelectSize != null) {
                        if (Math.abs(deviceHeight - itemSize.width) < Math.abs(deviceHeight - currentSelectSize.width)) {
                            currentSelectSize = itemSize;
                            continue;
                        }
                    } else {
                        currentSelectSize = itemSize;
                    }

                }

            }
        }
        return currentSelectSize;
    }

    public void startRecorder() {
        if (!isRecorder) {
            mCamera.stopPreview();
            configMedioRecorder();
            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mMediaRecorder.start();
            isRecorder = true;
        }
    }

    public void stopRecorder() {
        if (isRecorder) {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            try {
                mCamera.setPreviewTexture(mTextureView.getSurfaceTexture());
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStart:
                startRecorder();
                break;
            case R.id.buttonStop:
                stopRecorder();
                if(isRecorder){
                    isRecorder = false;
                    Intent intent = new Intent(this, VideoInfoCollectionActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaRecorder != null) {
            if (isRecorder) {
                mMediaRecorder.stop();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

        }
    }

    //    private void upLoadFireBase() {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        Uri file = Uri.fromFile(videoFile);
//        StorageReference videosRef = storageRef.child("videos/" + file.getLastPathSegment());
//        videosRef.putFile(file)
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                })
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    }
//                });
//    }
//


    private boolean isPreviewActive=true;// Whether preview is active. Prevent abnormalities when calling focus when preview is inactive.

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isPreviewActive) {// Auto focus function can only be called when preview is active
            // focus
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.cancelAutoFocus();
                }
            });
        }
        return super.onTouchEvent(event);
    }
}
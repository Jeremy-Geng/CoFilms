package com.cognidius.cofilms.activities.player;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.database.room.Video;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("deprecation")
public class CustomCameraActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = CustomCameraActivity.class.getSimpleName();
    private MediaRecorder mMediaRecorder;
    private TextureView mTextureView;
    private Button bStart, bStop;
    private android.hardware.Camera mCamera;
    private android.hardware.Camera.Size mSelectSize;
    private boolean isRecorder = false;
    private File videoFile;
    private ImageView cameraSwitch;
    private boolean faceCamera = false;
    private ImageView backtoUserMenu;
    private Video currentVideo;

    //auto focus
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x01:
                    mCamera.autoFocus(new android.hardware.Camera.AutoFocusCallback() { //auto focus
                        @Override
                        public void onAutoFocus(boolean success, android.hardware.Camera camera) {

                        }
                    });
                    mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);// per 2s, auto focus repeatedly
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_domain);
        // dynamic camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // here, Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
        }
        // dynamic audio_record permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);

        }
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
                    mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        initMediaRecorder();

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
                    mHandler.sendEmptyMessageDelayed(0x01, 2 * 1000);
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
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);//关闭闪光灯
            parameters.setFocusMode(android.hardware.Camera.Parameters.FLASH_MODE_AUTO); //对焦设置为自动
        }
        parameters.setPreviewSize(mSelectSize.width, mSelectSize.height);//设置预览尺寸
        parameters.setPictureSize(mSelectSize.width, mSelectSize.height);//设置图片尺寸  就拿预览尺寸作为图片尺寸,其实他们基本上是一样的
        parameters.set("orientation", "portrait");//相片方向
        parameters.set("rotation", 90); //相片镜头角度转90度（默认摄像头是横拍）
        mCamera.setParameters(parameters);//添加参数
        mCamera.setDisplayOrientation(90);//设置显示方向
    }

    private void initMediaRecorder() {
        mMediaRecorder = new MediaRecorder();
    }

    private void configMedioRecorder() {
        int rotation = 0;
        long timeStamp = System.currentTimeMillis();
        String videoId = Long.toString(timeStamp);
        videoFile = new File(getExternalCacheDir(), videoId+".mp4");
        currentVideo =  new Video(videoId,"Question");
        VideoInfoCollectionActivity.setCurrentVideo(currentVideo);
        mCamera.unlock();

        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//设置音频源
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);//设置视频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置音频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置音频编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);//设置视频编码格式
        mMediaRecorder.setVideoSize(mSelectSize.width, mSelectSize.height);//设置视频分辨率
        mMediaRecorder.setVideoEncodingBitRate(8 * 1920 * 1080);//设置视频的比特率
        mMediaRecorder.setVideoFrameRate(30);//设置视频的帧率
        rotation = faceCamera ? 270 : 90;
        mMediaRecorder.setOrientationHint(rotation);//设置视频的角度
        mMediaRecorder.setMaxDuration(60 * 1000);//设置最大录制时间
        Surface surface = new Surface(mTextureView.getSurfaceTexture());
        mMediaRecorder.setPreviewDisplay(surface);//设置预览
        mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());//设置文件保存路径
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
}
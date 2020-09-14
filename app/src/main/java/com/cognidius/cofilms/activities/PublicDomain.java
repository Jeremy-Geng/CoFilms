package com.cognidius.cofilms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.icu.text.SimpleDateFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

public class PublicDomain extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = PublicDomain.class.getSimpleName();
    private MediaRecorder mMediaRecorder;
    private TextureView mTextureView;
    private Button bStart, bStop;
    private Camera mCamera;
    private Camera.Size mSelectSize;
    private boolean isRecorder = false;
    private File videoFile;
    //auto focus
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x01:
                    mCamera.autoFocus(new Camera.AutoFocusCallback() { //auto focus
                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {

                        }
                    });
                    mHandler.sendEmptyMessageDelayed(0x01,2*1000);// per 2s, auto focus repeatedly
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
        mTextureView = findViewById(R.id.textureView);
        bStart = findViewById(R.id.buttonStart);
        bStop = findViewById(R.id.buttonStop);
        bStart.setOnClickListener(this);
        bStop.setOnClickListener(this);
        initTextureViewListener();
        initMediaRecorder();
    }


    private void initTextureViewListener(){
        mTextureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                initCamera();
                if(mCamera == null) {
                    Log.e(TAG,"Set camera fail");
                }else{
                    Log.e(TAG,"Set camera success");

                }
                initCameraConfig();
                try {
                    mCamera.setPreviewTexture(surfaceTexture);
                    mCamera.startPreview();
                    mHandler.sendEmptyMessageDelayed(0x01,2*1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });
    }

    private Integer selectCamera(boolean isFacing){
        int cameraCount = Camera.getNumberOfCameras();
//        CameraInfo.CAMERA_FACING_BACK
//        CameraInfo.CAMERA_FACING_FRONT
        int facing = isFacing ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK;
        Log.e(TAG, "selectCamera: cameraCount="+cameraCount);
        if (cameraCount == 0){
            Log.e(TAG, "selectCamera: The device does not have a camera ");
            return null;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i=0; i < cameraCount; i++){
            Camera.getCameraInfo(i,info);
            if (info.facing == facing){
                return i;
            }

        }
        return null;

    }

    private void initCamera(){
        mCamera = Camera.open(selectCamera(false));
        mSelectSize = selectPreviewSize(mCamera.getParameters());
    }

    private void initCameraConfig(){
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);//关闭闪光灯
        parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO); //对焦设置为自动
        parameters.setPreviewSize(mSelectSize.width,mSelectSize.height);//设置预览尺寸
        parameters.setPictureSize(mSelectSize.width,mSelectSize.height);//设置图片尺寸  就拿预览尺寸作为图片尺寸,其实他们基本上是一样的
        parameters.set("orientation", "portrait");//相片方向
        parameters.set("rotation", 90); //相片镜头角度转90度（默认摄像头是横拍）
        mCamera.setParameters(parameters);//添加参数
        mCamera.setDisplayOrientation(90);//设置显示方向

    }

    private void initMediaRecorder(){
        mMediaRecorder = new MediaRecorder();
    }

    private void configMedioRecorder(){
        long timeStamp = System.currentTimeMillis();
        File saveRecorderFile = new File(getExternalCacheDir(),timeStamp + "CameraRecorder.mp4");
        
        if (saveRecorderFile.exists()){
            saveRecorderFile.delete();
        }
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);//设置音频源
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);//设置视频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);//设置音频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);//设置音频编码格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);//设置视频编码格式
        mMediaRecorder.setVideoSize(mSelectSize.width,mSelectSize.height);//设置视频分辨率
        mMediaRecorder.setVideoEncodingBitRate(8*1920*1080);//设置视频的比特率
        mMediaRecorder.setVideoFrameRate(30);//设置视频的帧率
        mMediaRecorder.setOrientationHint(90);//设置视频的角度
        mMediaRecorder.setMaxDuration(60*1000);//设置最大录制时间
        Surface surface = new Surface(mTextureView.getSurfaceTexture());
        mMediaRecorder.setPreviewDisplay(surface);//设置预览
        mMediaRecorder.setOutputFile(saveRecorderFile.getAbsolutePath());//设置文件保存路径
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

    private Camera.Size selectPreviewSize(Camera.Parameters parameters){
        List<Camera.Size> previewSizeList =  parameters.getSupportedPreviewSizes();
        if (previewSizeList.size() == 0){
            Log.e(TAG, "selectPreviewSize: previewSizeList size is 0" );
            return null;

        }

        Camera.Size currentSelectSize = null;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int deviceWidth =displayMetrics.widthPixels;
        int deviceHeight = displayMetrics.heightPixels;
        for (int i = 1; i < 41 ; i++){
            for(Camera.Size itemSize : previewSizeList){
                if (itemSize.height > (deviceWidth - i*5) && itemSize.height < (deviceWidth + i*5)){
                    if (currentSelectSize != null){
                        if (Math.abs(deviceHeight-itemSize.width) < Math.abs(deviceHeight - currentSelectSize.width)){
                            currentSelectSize = itemSize;
                            continue;
                        }
                    }else {
                        currentSelectSize = itemSize;
                    }

                }

            }
        }
        return currentSelectSize;
    }

    public void startRecorder(){
        if(!isRecorder){
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

    public void stopRecorder(){
        if(isRecorder){
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            isRecorder =false;
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
        switch (view.getId()){
            case R.id.buttonStart:
                startRecorder();
                break;
            case R.id.buttonStop:
                stopRecorder();
                Intent intent = new Intent(this, MediaPlayerActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaRecorder != null){
            if (isRecorder) {
                mMediaRecorder.stop();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;

        }
    }

    private void upLoadFireBase(){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();


    }


}
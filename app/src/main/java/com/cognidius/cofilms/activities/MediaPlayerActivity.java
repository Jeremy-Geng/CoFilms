package com.cognidius.cofilms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;

import java.io.File;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView mSurfaceView;
    private MediaPlayer mediaPlayer;
    private Button btnStartAndStop;
    private String path;
    private boolean isInitFinish = false;
    private SurfaceHolder mSurfaceHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        initActivity();
        initMediaPlayer();
        initSurfaceviewStateListener();

    }

    //Initialize this activity
    private void initActivity() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        btnStartAndStop = (Button) findViewById(R.id.buttonStartAndStop);
        btnStartAndStop.setOnClickListener(this);
        File file = new File(getExternalCacheDir(), "CameraRecorder.mp4");
        path = file.getAbsolutePath();
        System.out.println("视频地址：" + path.toString());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    private void setMediaPlayer(String path) {
        try {
            String url = "https://firebasestorage.googleapis.com/v0/b/cofims.appspot.com/o/videos%2F1600058689745CameraRecorder.mp4?alt=media&token=5131ff8c-76ff-450d-9a3d-deb162fbbb70";
            mediaPlayer.setDataSource(url);
            mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mediaPlayer.setLooping(true);
            System.out.println("设置路径及同步成功");
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    isInitFinish = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startPlay() {
        if (!mediaPlayer.isPlaying() && isInitFinish) {
            mediaPlayer.start();
        }
    }

    private void stopPlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private void pausePlay() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void seekTo(int time) {
        mediaPlayer.seekTo(time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonStartAndStop:
                if (mediaPlayer.isPlaying()) {
                    pausePlay();
                } else {
                    System.out.println("开始播放");
                    startPlay();
                }
                break;
            default:
                break;
        }

    }

    public void initSurfaceviewStateListener() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setDisplay(holder);
                setMediaPlayer(path);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }

            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
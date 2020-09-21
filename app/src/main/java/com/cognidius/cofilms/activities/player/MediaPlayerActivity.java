package com.cognidius.cofilms.activities.player;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.cognidius.cofilms.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private SurfaceView mSurfaceView;
    private MediaPlayer mediaPlayer;
    private FloatingActionButton btnStartAndStop;
    private String path;
    private boolean isInitFinish = false;
    private SurfaceHolder mSurfaceHolder;
    private ProgressBar progressBar;

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
        mSurfaceView = findViewById(R.id.surfaceView);
        btnStartAndStop = findViewById(R.id.fabControl);
        progressBar = findViewById(R.id.progressBar);
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

    private void setMediaPlayer(String path)  {
        try {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            final Uri videoUri = null;
            storageRef.child("videos/1600058580498CameraRecorder.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                @Override
                public void onSuccess(Uri uri){
                        System.out.println(uri.toString());


                }
            });

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
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    startPlay();
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
        if(mediaPlayer.isPlaying()){
            pausePlay();
        }else{
            startPlay();
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
package com.cognidius.cofilms.activities.player;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.database.room.Video;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.io.File;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private FloatingActionButton btnStartAndStop, backTo, addOneAnswer;
    private ProgressBar progressBar;
    private VideoView videoView;
    private static Video currentVideo;
    private static boolean preProcess = false;
    private boolean isPrepared;
    private boolean isPlaying;
    private int numOfAnswers;
    private File[] answers;
    private int cursor;
    private AlertDialog.Builder playAnswerBuilder;
    private AlertDialog.Builder noAnswerBuilder;
    AlertDialog playAnswer ;
    AlertDialog noAnswer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initActivity();

    }

    public static void setCurrentVideo(Video currentVideo) {
        MediaPlayerActivity.currentVideo = currentVideo;
    }

    //Initialize this activity
    private void initActivity() {
        btnStartAndStop = findViewById(R.id.fabControl);
        backTo = findViewById(R.id.fabBackTo);
        addOneAnswer = findViewById(R.id.fabAddAnswer);
        progressBar = findViewById(R.id.progressBar);
        videoView = findViewById(R.id.videoView);
        isPrepared = false;
        isPlaying = false;
        playAnswerBuilder = new AlertDialog.Builder(this);
        noAnswerBuilder = new AlertDialog.Builder(this);
        cursor = 1;

        if(preProcess){
            videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.test_question));
            preProcess = false;
        }else{
            File videoFile = new File(getExternalCacheDir(),"/" + currentVideo.getVideoId() + "/Question.mp4");
            videoView.setVideoPath(videoFile.getAbsolutePath());
            File path = new File(getExternalCacheDir(),"/" + currentVideo.getVideoId());
            answers = path.listFiles();
            System.out.println("-------" + answers[0].getName());
            numOfAnswers = answers.length;
        }

        // Set two dialogs
        playAnswerBuilder.setMessage(R.string.wanna_see_answer)
                  .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          videoView.setVideoPath(answers[cursor].getAbsolutePath());
                          videoView.start();
                          isPlaying = true;
                          cursor++;
                      }
                  })
                  .setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          videoView.start();
                          isPlaying = true;
                      }
                  });

        noAnswerBuilder.setMessage(R.string.no_answer)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        videoView.start();
                        isPlaying = true;
                    }
                });

        playAnswer = playAnswerBuilder.create();
        noAnswer = noAnswerBuilder.create();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                isPrepared = true;
                isPlaying = true;
                progressBar.setVisibility(ProgressBar.INVISIBLE);
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(numOfAnswers == 1 || cursor == numOfAnswers){
                    noAnswer.show();
                }else{
                    playAnswer.show();
                }
            }
        });

        btnStartAndStop.setOnClickListener(this);
        backTo.setOnClickListener(this);
        addOneAnswer.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabControl:
                if(isPrepared){
                    if(isPlaying){
                        videoView.pause();
                        isPlaying = false;
                    }else{
                        videoView.start();
                        isPlaying = true;
                    }
                }
                break;
            case R.id.fabBackTo:
                Intent intent = new Intent(MediaPlayerActivity.this, UserMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.fabAddAnswer:
                Intent intentTwo = new Intent(MediaPlayerActivity.this, CustomCameraActivity.class);
                String parentId = currentVideo.getParentId() == null ? currentVideo.getVideoId() : currentVideo.getParentId();
                intentTwo.putExtra("parentId", parentId);
                intentTwo.putExtra("videoType","answer");
                startActivity(intentTwo);
                break;
            default:


        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void setPreProcess(boolean preProcess) {
        MediaPlayerActivity.preProcess = preProcess;
    }
}
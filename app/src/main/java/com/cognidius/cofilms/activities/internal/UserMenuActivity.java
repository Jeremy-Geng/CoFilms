package com.cognidius.cofilms.activities.internal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.player.CustomCameraActivity;
import com.cognidius.cofilms.database.LoggedUser;
import com.cognidius.cofilms.database.room.InitialDataBase;
import com.cognidius.cofilms.database.room.Video;
import com.cognidius.cofilms.database.room.dao.VideoDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UserMenuActivity extends AppCompatActivity {
    private Button gotoPublic;
    private RecyclerView videoList;
    private FloatingActionButton fabAddVideo;
    private VideoDao videoDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initView();
    }

    private void initView(){
        gotoPublic = findViewById(R.id.Public);
        gotoPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenuActivity.this, PublicAreaActivity.class);
                startActivity(intent);
            }
        });

        fabAddVideo = findViewById(R.id.fabAddVideo);
        fabAddVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserMenuActivity.this, CustomCameraActivity.class);
                startActivity(intent);
            }
        });

        //Video testVideo = new Video("20201005","Tomt","question");
        videoDao = InitialDataBase.initialVideoTable(this);
        //videoDao.insertAll(testVideo);
        List<Video> videos = videoDao.loadQuestionsRelatedToSpecificUser(LoggedUser.getUSERNAME(), "question");
        videoList = findViewById(R.id.videosRecView);
        VideoAdapter adapter = new VideoAdapter(this);
        adapter.setVideoList(videos);
        videoList.setAdapter(adapter);
        videoList.setLayoutManager(new GridLayoutManager(this,2));
    }




}
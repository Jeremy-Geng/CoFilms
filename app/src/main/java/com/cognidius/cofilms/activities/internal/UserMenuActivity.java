package com.cognidius.cofilms.activities.internal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.player.CustomCamera;
import com.cognidius.cofilms.database.Video;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class UserMenuActivity extends AppCompatActivity {
    private static String username;
    private Button gotoPublic;
    private RecyclerView videoList;
    private FloatingActionButton fabAddVideo;


    public static void setUsername(String username) {
        UserMenuActivity.username = username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
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
                Intent intent = new Intent(UserMenuActivity.this, CustomCamera.class);
                startActivity(intent);
            }
        });

        ArrayList<Video> videos = new ArrayList<>();
        videos.add(new Video("1","Question1","Question"));
        videos.add(new Video("2","Answer1","Answer"));

        videoList = findViewById(R.id.videosRecView);
        VideoAdapter adapter = new VideoAdapter(this);
        adapter.setVideoList(videos);
        videoList.setAdapter(adapter);
        videoList.setLayoutManager(new GridLayoutManager(this,2));



    }

    private void getVideos(){

    }


}
package com.cognidius.cofilms.activities.internal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.player.CustomCameraActivity;
import com.cognidius.cofilms.activities.player.MediaPlayerActivity;
import com.cognidius.cofilms.activities.player.VideoInfoCollectionActivity;
import com.cognidius.cofilms.database.LoggedUser;
import com.cognidius.cofilms.database.VideoInfo;
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
    private VideoAdapter adapter;
    private AlertDialog.Builder autoPlayingBuilder;
    AlertDialog autoPlaying;

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
                VideoInfoCollectionActivity.setIsQuestion(true);
                Intent intent = new Intent(UserMenuActivity.this, CustomCameraActivity.class);
                startActivity(intent);
            }
        });

        autoPlayingBuilder = new AlertDialog.Builder(this);

        autoPlayingBuilder.setMessage("Wanna auto-play answers?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MediaPlayerActivity.setIsAutoPlaying(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MediaPlayerActivity.setIsAutoPlaying(false);
                    }
                });
        autoPlaying = autoPlayingBuilder.create();

        //Video testVideo = new Video("20201005","Tomt","question");
        videoDao = InitialDataBase.initialVideoTable(this);
        //videoDao.insertAll(testVideo);
        List<Video> videos = videoDao.loadQuestionsRelatedToSpecificUser(LoggedUser.getUSERNAME(), "question");

        videoList = findViewById(R.id.videosRecView);
        adapter = new VideoAdapter(this);
        adapter.setVideoList(videos);
        videoList.setAdapter(adapter);
        videoList.setLayoutManager(new GridLayoutManager(this,2));
        autoPlaying.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_menu,menu);
        MenuItem item  = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private List<Integer> countAnswers(){
        List<Integer> countAnswers = new ArrayList<>();


        return countAnswers;
    }
}
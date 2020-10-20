package com.cognidius.cofilms.activities.internal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.database.room.InitialDataBase;
import com.cognidius.cofilms.database.room.Video;
import com.cognidius.cofilms.database.room.dao.VideoDao;

import java.util.List;

public class PublicAreaActivity extends AppCompatActivity {
    private Button gotoUser;
    private RecyclerView videoListForPublic;
    private VideoDao videoDao;
    private VideoAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_area);
        initView();
    }

    private void initView(){
        gotoUser = findViewById(R.id.User);
        gotoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PublicAreaActivity.this, UserMenuActivity.class);
                startActivity(intent);
            }
        });

        //Video testVideo = new Video("20201005","Tomt","question");
        videoDao = InitialDataBase.initialVideoTable(this);
        //videoDao.insertAll(testVideo);
        List<Video> videos = videoDao.loadAllQuestions( "question");
        videoListForPublic = findViewById(R.id.videosRecViewForPublic);
        adapter = new VideoAdapter(this);
        adapter.setVideoList(videos);
        videoListForPublic.setAdapter(adapter);
        videoListForPublic.setLayoutManager(new GridLayoutManager(this,2));
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


}
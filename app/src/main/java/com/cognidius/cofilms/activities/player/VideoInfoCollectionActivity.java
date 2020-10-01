package com.cognidius.cofilms.activities.player;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.database.Azure.AzureConnection;
import com.cognidius.cofilms.database.room.Video;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class VideoInfoCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private static Video currentVideo;
    private EditText videoTitle;
    private RadioGroup videoType;
    private Button btnConfirm;
    private Button btnCancel;
    private RadioButton checckedType;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info_collection);
        initActivity();
    }

    private void initActivity(){
        videoTitle = findViewById(R.id.videoTitle);
        videoType = findViewById(R.id.videoType);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.progressBar);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        videoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                selectrb();
            }

            private void selectrb(){
                checckedType = findViewById(videoType.getCheckedRadioButtonId());
                currentVideo.setVideoType(checckedType.getText().toString());
            }
        });

    }

//    private void uploadVideoToFirebase(){
//        File videoFile;
//        videoFile = new File(getExternalCacheDir(), currentVideo.getVideoId()+".mp4");
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
//                        Toast.makeText(VideoInfoCollectionActivity.this,"Upload Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(VideoInfoCollectionActivity.this, UserMenuActivity.class);
//                        startActivity(intent);
//                    }
//                });
//
//    }

    public void uploadVideoToAzure() {
        PreparedStatement prepSt = AzureConnection.insert("Videoinfo");
        try {
            prepSt.setString(1, currentVideo.getVideoId());
            prepSt.setString(2, currentVideo.getVideoTitle());
            prepSt.setString(3, currentVideo.getVideoType());
            prepSt.setString(4, currentVideo.getBelongTo());
            prepSt.setNull(5, Types.VARCHAR);
            ResultSet rs = prepSt.executeQuery();
            System.out.println("Insert Video info successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void setCurrentVideo(Video video){
        currentVideo = video;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnConfirm:
//                currentVideo.setVideoTitle(videoTitle.getText().toString());
//                currentVideo.setBelongTo(LoggedUser.getUSERNAME());
                progressBar.setVisibility(ProgressBar.VISIBLE);
                //uploadVideoToAzure();
                //uploadVideoToFirebase();


                break;
            case R.id.btnCancel:
                Intent intent = new Intent(VideoInfoCollectionActivity.this, CustomCameraActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
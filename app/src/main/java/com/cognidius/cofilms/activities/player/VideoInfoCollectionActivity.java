package com.cognidius.cofilms.activities.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.database.Azure.AzureConnection;
import com.cognidius.cofilms.database.room.InitialDataBase;
import com.cognidius.cofilms.database.room.User;
import com.cognidius.cofilms.database.room.Video;
import com.cognidius.cofilms.database.room.dao.UserDao;
import com.cognidius.cofilms.database.room.dao.VideoDao;


import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class VideoInfoCollectionActivity extends AppCompatActivity implements View.OnClickListener {
    private static Video currentVideo;
    private static boolean isQuestion;
    private EditText videoTitle, videoDescription, mentionedOtherUsers;
    private ImageView thumbNailImage;
    // private RadioGroup videoType;
    private Button btnConfirm;
    private Button btnCancel;
    private boolean isImageSet = false;
    // private RadioButton checckedType;
    //private ProgressBar progressBar;
    VideoDao videoDao;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info_collection);
        initActivity();
    }

    private void initActivity() {
        videoTitle = findViewById(R.id.videoTitle);
        //videoType = findViewById(R.id.videoType);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnCancel = findViewById(R.id.btnCancel);
        thumbNailImage = findViewById(R.id.thumbNailImage);
        if(!isQuestion) thumbNailImage.setVisibility(View.INVISIBLE);
        //progressBar = findViewById(R.id.progressBar);
        btnConfirm.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        thumbNailImage.setOnClickListener(this);
        videoDao = InitialDataBase.initialVideoTable(this);


        //videoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                selectrb();
//            }
//
//            private void selectrb(){
//                checckedType = findViewById(videoType.getCheckedRadioButtonId());
//                currentVideo.setVideoType(checckedType.getText().toString());
//            }
//        });
//

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

    public void uploadVideoInfo() {
//        PreparedStatement prepSt = AzureConnection.insert("Videoinfo");
//        try {
//            prepSt.setString(1, currentVideo.getVideoId());
//            prepSt.setString(2, currentVideo.getVideoTitle());
//            prepSt.setString(3, currentVideo.getVideoType());
//            prepSt.setString(4, currentVideo.getBelongTo());
//            prepSt.setNull(5, Types.VARCHAR);
//            ResultSet rs = prepSt.executeQuery();
//            System.out.println("Insert Video info successfully");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        videoDao.insertAll(currentVideo);
    }

    public static void setCurrentVideo(Video video) {
        currentVideo = video;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnConfirm:
                currentVideo.setVideoTitle(videoTitle.getText().toString());
                //progressBar.setVisibility(ProgressBar.VISIBLE);
                uploadVideoInfo();
                //uploadVideoToFirebase();
                Toast.makeText(VideoInfoCollectionActivity.this, "Upload Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VideoInfoCollectionActivity.this, UserMenuActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCancel:
                Intent intentTwo = new Intent(VideoInfoCollectionActivity.this, CustomCameraActivity.class);
                if (currentVideo.getVideoType().equals("question")) {
                    File videoFile = new File(getExternalCacheDir(), "/" + currentVideo.getVideoId() + "/Question.mp4");
                    videoFile.delete();
                    File path = new File(getExternalCacheDir(), "/" + currentVideo.getVideoId());
                    path.delete();
                } else {
                    File videoFile = new File(getExternalCacheDir(), "/" + currentVideo.getParentId() + "/" + currentVideo.getVideoId() + ".mp4");
                    videoFile.delete();
                    intentTwo.putExtra("videoId", currentVideo.getParentId());
                    intentTwo.putExtra("videoType", false);
                }
                startActivity(intentTwo);
            case R.id.thumbNailImage:
                if (!isImageSet) {
                    //check runtime permission
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_DENIED) {
                            //permission not granted, request it
                            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                            //show popup for runtime permision
                            requestPermissions(permissions, PERMISSION_CODE);
                        } else {
                            // permission already granted
                            pickImageFromGallery();
                        }
                    } else {
                        //system os is less marshmallow
                        pickImageFromGallery();
                    }
                    isImageSet = true;
                }
                break;
            default:
        }
    }

    public static void setIsQuestion(boolean isQuestion) {
        VideoInfoCollectionActivity.isQuestion = isQuestion;
    }

    public void pickImageFromGallery() {
        //intent to pick image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted
                    pickImageFromGallery();
                } else {
                    //permission was denied
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // set image to imageview
            if (data != null) {
                thumbNailImage.setImageURI(data.getData());
                if (data.getData() != null) currentVideo.setThumbnailUrl(data.getData().toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
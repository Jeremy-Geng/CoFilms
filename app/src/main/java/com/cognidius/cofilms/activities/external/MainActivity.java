package com.cognidius.cofilms.activities.external;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.activities.player.MediaPlayerActivity;
import com.cognidius.cofilms.auxiliary.CommunitySelection;
import com.cognidius.cofilms.database.Azure.AzureConnection;
import com.cognidius.cofilms.database.LoggedUser;
import com.cognidius.cofilms.database.UserInfo;
import com.cognidius.cofilms.database.contract.UserInfoContract;
import com.cognidius.cofilms.database.room.InitialDataBase;
import com.cognidius.cofilms.database.room.User;
import com.cognidius.cofilms.database.room.dao.UserDao;


import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView officialWebsite;
    TextView signUp;
    EditText userName;
    EditText password;
    ImageView logIn;
    UserDao userDao;
    List<User> users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_main);
        //AzureConnection.connect();
       // AzureConnection.getUsernameList();
        // dynamic camera permission
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            // here, Permission is not granted
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 50);
//        }
//
//        // dynamic audio_record permission
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
//
//        }

        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionList = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.
                    permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.
                    permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.RECORD_AUDIO);
            }
            if (!permissionList.isEmpty()) {
                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
                ActivityCompat.requestPermissions(this, permissions, 1);
            } else {

            }
        }


            initView();
    }


    private void initView() {
        logIn = findViewById(R.id.logIn);
        userName = findViewById(R.id.Username);
        password = findViewById(R.id.Password);

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameP = userName.getText().toString();
                String passwordP = password.getText().toString();

                if(!isUserExisting(userNameP)){
                    Toast.makeText(MainActivity.this,"Invalid Username!", Toast.LENGTH_SHORT).show();
                }else if(!checkPassWord(userNameP,passwordP)){
                    Toast.makeText(MainActivity.this,"Incorrect Password!", Toast.LENGTH_SHORT).show();
                }else{
                    LoggedUser.setUSERNAME(userNameP);
                    LoggedUser.setPASSWORD(passwordP);
                    Intent intent = new Intent(MainActivity.this, CommunityActivity.class);
                    startActivity(intent);
                }
            }
        });

        signUp = findViewById(R.id.SignUp);
        officialWebsite = findViewById(R.id.textOfficialSite);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        officialWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cognidius.com/"));
                startActivity(intent);
            }
        });

        // Create a default user for testing convenience
        User defaultUser = new User("Tomt","1234");
        userDao = InitialDataBase.initialUserTable(this);
        users = userDao.loadAllUsers();
        if(users.size() == 0) {
            userDao.insertAll(defaultUser);
            users = userDao.loadAllUsers();
        }
        System.out.println("----Users:" + users);

    }

    private boolean isUserExisting(String inputUserName){
        boolean rst = false;
        for (User user : users) {
            if(user.getUserName().equals(inputUserName)){
                rst = true;
                break;
            }
        }
        return rst;
    }

    private boolean checkPassWord(String inputUserName, String inputPassword){
        boolean rst = false;
        for (User user : users) {
            if(user.getUserName().equals(inputUserName)){
                if(user.getPassWord().equals(inputPassword)){
                    rst = true;
                    break;
                }
            }
        }
        return rst;
    }


    public void setStatusBarTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) { // 5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public void setUpDatabase() {
        UserInfo userInfo = new UserInfo(this);
        SQLiteDatabase sqLiteDatabase = userInfo.getWritableDatabase();
        userInfo.onCreate(sqLiteDatabase);

        ContentValues values = new ContentValues();
        values.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME, "Shuhao Geng");
        sqLiteDatabase.insert(UserInfoContract.UserInfoEntry.TABLE_NAME, null, values);

        sqLiteDatabase = userInfo.getReadableDatabase();
        String[] projection = {UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME};
        String selection = UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {"Shuhao Geng"};
        Cursor cursor = sqLiteDatabase.query(UserInfoContract.UserInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        String usrname = "";
        while (cursor.moveToNext()) {
            usrname = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME));
        }

        cursor.close();

        EditText editText = findViewById(R.id.Username);
        editText.setText(usrname);
    }
}
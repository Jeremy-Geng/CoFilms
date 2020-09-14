 package com.cognidius.cofilms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.cognidius.cofilms.R;
import com.cognidius.cofilms.database.UserInfo;
import com.cognidius.cofilms.database.contract.UserInfoContract;

 public class MainActivity extends AppCompatActivity {
     UserInfo userInfo;
     TextView officialWebsite;
     TextView signUp;
     EditText userName;
     EditText password;
     ImageView logIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_main);
        initView();



    }

    private void initView(){
        logIn = findViewById(R.id.logIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PublicDomain.class);
                startActivity(intent);
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



    }


    public void setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){ // 4.4
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

    public void setUpDatabase(){
        UserInfo userInfo  = new UserInfo(this);
        SQLiteDatabase sqLiteDatabase  = userInfo.getWritableDatabase();
        userInfo.onCreate(sqLiteDatabase);

        ContentValues values = new ContentValues();
        values.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME,"Shuhao Geng");
        sqLiteDatabase.insert(UserInfoContract.UserInfoEntry.TABLE_NAME, null,values);

        sqLiteDatabase = userInfo.getReadableDatabase();
        String[] projection = {UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME};
        String selection = UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME + " = ?";
        String [] selectionArgs = {"Shuhao Geng"};
        Cursor cursor = sqLiteDatabase.query(UserInfoContract.UserInfoEntry.TABLE_NAME, projection, selection,selectionArgs,null,null,null);

        String usrname = "";
        while(cursor.moveToNext()){
            usrname = cursor.getString(cursor.getColumnIndexOrThrow(UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME));
        }

        cursor.close();

        EditText editText = findViewById(R.id.Username);
        editText.setText(usrname);
    }
}
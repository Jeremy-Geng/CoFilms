 package com.cognidius.cofilms.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.database.UserInfo;
import com.cognidius.cofilms.database.contract.UserInfoContract;

 public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTransparent();
        setContentView(R.layout.activity_main);

        UserInfo userInfo  = new UserInfo(this);
        SQLiteDatabase sqLiteDatabase  = userInfo.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME,"Shuhao Geng");
        sqLiteDatabase.insert(UserInfoContract.UserInfoEntry.TABLE_NAME, null,values);

        sqLiteDatabase = userInfo.getReadableDatabase();
        String[] projection = {UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME};
        String selection = UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME + " = ?";
        String [] selectionArgs = {"Shuhao Geng"};
        Cursor cursor = sqLiteDatabase.query(UserInfoContract.UserInfoEntry.TABLE_NAME,
                                                                            projection,
                selection,selectionArgs,null,null,null);

        String usrname = "";
        while(cursor.moveToNext()){
            usrname = cursor.getString(0);
        }

        cursor.close();

        EditText editText = findViewById(R.id.Username);
        editText.setText(usrname);
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
}
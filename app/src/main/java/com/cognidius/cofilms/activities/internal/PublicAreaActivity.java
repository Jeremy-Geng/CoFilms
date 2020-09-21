package com.cognidius.cofilms.activities.internal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cognidius.cofilms.R;

public class PublicAreaActivity extends AppCompatActivity {
    private Button gotoUser;

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
    }


}
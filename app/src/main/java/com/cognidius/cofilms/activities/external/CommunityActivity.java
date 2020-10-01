package com.cognidius.cofilms.activities.external;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.activities.internal.UserMenuActivity;
import com.cognidius.cofilms.auxiliary.CommunitySelection;
import com.google.android.material.textview.MaterialTextView;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {
    private MaterialTextView education, entertainment, sports, politics;
    private ImageView rightArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);
        initView();
    }

    private void initView(){
        education = findViewById(R.id.tvEducation);
        entertainment = findViewById(R.id.tvEntertainment);
        sports = findViewById(R.id.tvSports);
        politics = findViewById(R.id.tvPolitics);
        rightArrow = findViewById(R.id.right_arrow);

        education.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        sports.setOnClickListener(this);
        politics.setOnClickListener(this);
        rightArrow.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvEducation:
                if(!CommunitySelection.isIsEducation()){
                    education.setBackgroundResource(R.drawable.border_on_click);
                    education.setTextColor(getResources().getColor(R.color.normal));
                }else{
                    education.setBackgroundResource(R.drawable.border);
                    education.setTextColor(getResources().getColor(R.color.pureBlack));

                }
                CommunitySelection.touchEducation();
                break;

            case R.id.tvEntertainment:
                if(!CommunitySelection.isIsEntertainment()){
                    entertainment.setBackgroundResource(R.drawable.border_on_click);
                    entertainment.setTextColor(getResources().getColor(R.color.normal));
                }else{
                    entertainment.setBackgroundResource(R.drawable.border);
                    entertainment.setTextColor(getResources().getColor(R.color.pureBlack));
                }

                CommunitySelection.touchEntertainment();
                break;

            case R.id.tvSports:
                if(!CommunitySelection.isIsSports()){
                    sports.setBackgroundResource(R.drawable.border_on_click);
                    sports.setTextColor(getResources().getColor(R.color.normal));
                }else{
                    sports.setBackgroundResource(R.drawable.border);
                    sports.setTextColor(getResources().getColor(R.color.pureBlack));

                }
                CommunitySelection.touchSports();
                break;

            case R.id.tvPolitics:
                if(!CommunitySelection.isIsPolitics()){
                    politics.setBackgroundResource(R.drawable.border_on_click);
                    politics.setTextColor(getResources().getColor(R.color.normal));
                }else{
                    politics.setBackgroundResource(R.drawable.border);
                    politics.setTextColor(getResources().getColor(R.color.pureBlack));

                }
                CommunitySelection.touchPolitics();
                break;

            case R.id.right_arrow:
                Intent intent = new Intent(this, UserMenuActivity.class);
                startActivity(intent);
                break;
            default:

        }
    }
}
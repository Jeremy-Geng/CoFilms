package com.cognidius.cofilms.activities.external;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cognidius.cofilms.R;
import com.cognidius.cofilms.database.Azure.AzureConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    ImageView backToLogIn;
    EditText userName, password, retypePassword;

    // p stands for pre, waiting to be checked
    String userNameP, passwordP, retypePasswordP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setStatusBarTransparent();
        initAcitivty();
    }

    private void initAcitivty() {
        userName = findViewById(R.id.editTextUserName);
        password = findViewById(R.id.editTextPassword);
        retypePassword = findViewById(R.id.editTextRetype);
        backToLogIn = findViewById(R.id.backToLogin);
        backToLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameP = userName.getText().toString();
                passwordP = password.getText().toString();
                retypePasswordP = retypePassword.getText().toString();
                isValidUserInfo();
            }
        });
    }

    public void isValidUserInfo() {
        String patternSpace = ".*\\s+.*";
        String patternSC = ".*[`~!@#_$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）— +|{}【】‘；：”“’。，、？].*";

        if (userNameP.equals("")) {
            Toast.makeText(SignUpActivity.this, "Please Input Your Username!", Toast.LENGTH_SHORT).show();
        } else if (Pattern.matches(patternSpace, userNameP) || Pattern.matches(patternSC, userNameP)) {
            Toast.makeText(SignUpActivity.this, "Username can only include characters and numbers!", Toast.LENGTH_SHORT).show();
        } else if (passwordP.equals("") || retypePasswordP.equals("")) {
            Toast.makeText(SignUpActivity.this, "Please Input Your Password!", Toast.LENGTH_SHORT).show();
        } else if (!passwordP.equals(retypePasswordP)) {
            Toast.makeText(SignUpActivity.this, "Password Inconsistent!", Toast.LENGTH_SHORT).show();
        } else if (userNameP.length() > 16 || userName.length() < 4) {
            Toast.makeText(SignUpActivity.this, "User name must include 4-16 characters!", Toast.LENGTH_SHORT).show();
        } else if (passwordP.length() > 16 || passwordP.length() < 4) {
            Toast.makeText(SignUpActivity.this, "Password must include 4-16 characters!", Toast.LENGTH_SHORT).show();
        } else if (AzureConnection.usernameList.contains(userNameP)) {
            Toast.makeText(SignUpActivity.this, "Username Occupied!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignUpActivity.this, "Sign Up Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            uploadInfoToServer();
            startActivity(intent);
        }

    }


    public void uploadInfoToServer() {
        PreparedStatement prepSt = AzureConnection.insert("Userinfo");
        try {
            prepSt.setString(1, userNameP);
            prepSt.setString(2, passwordP);
            prepSt.setNull(3, Types.VARCHAR);
            prepSt.setNull(4, Types.VARCHAR);
            prepSt.setNull(5, Types.VARCHAR);
            prepSt.setNull(6, Types.VARCHAR);
            prepSt.setNull(7, Types.DATE);
            ResultSet rs = prepSt.executeQuery();
            System.out.println("Insert data successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
}
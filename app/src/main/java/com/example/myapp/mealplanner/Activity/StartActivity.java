package com.example.myapp.mealplanner.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapp.mealplanner.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_start);

        Button mRegBtn = findViewById(R.id.regBtn_start_Act);
        Button mLogBtn = findViewById(R.id.loginBtn_start_Act);

        mRegBtn.setOnClickListener(mOnClickListener);

        mLogBtn.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.regBtn_start_Act:
                    Intent registerIntent = new Intent(StartActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                    break;

                case R.id.loginBtn_start_Act:
                    Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;
            }
        }
    };
}

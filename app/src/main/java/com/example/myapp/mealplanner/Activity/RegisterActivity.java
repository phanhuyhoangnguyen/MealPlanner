package com.example.myapp.mealplanner.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapp.mealplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayName;
    private TextInputLayout mEmail;
    private TextInputLayout mPassword;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Find Widget
        mDisplayName = findViewById(R.id.nameInput_register_Act);
        mEmail = findViewById(R.id.emailInput_register_Act);
        mPassword = findViewById(R.id.passwordInput_login_Act);
        Button mCreateBtn = findViewById(R.id.regBtn_register_Act);

        //ToolBar Set
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Recipe Planner");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //todo: add progress bar
        ProgressBar mRegProgress = new ProgressBar(this);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String pwd = mPassword.getEditText().getText().toString();

                if (!(TextUtils.isEmpty(display_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd))){
                    //TODO: add progress bar
                    registerUser(display_name, email, pwd);
                }
            }
        });


    }

    private void registerUser(String display_name, String email, String pwd) {
        Log.i("email", email);
        Log.i("pwd", pwd);
        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            //TODO: add progress bar
                            Toast.makeText(RegisterActivity.this, "Register Error!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            //TODO: add progress bar
                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                });
    }
}

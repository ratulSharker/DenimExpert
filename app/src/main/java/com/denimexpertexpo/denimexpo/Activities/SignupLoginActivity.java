package com.denimexpertexpo.denimexpo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denimexpertexpo.denimexpo.R;
import com.denimexpertexpo.denimexpo.StaticStyling.CustomStyling;


public class SignupLoginActivity extends Activity {

    EditText mEdittextUsername,mEdittextPassword;
    Button mBtnRegister,mBtnLogin,mBtnSkiplogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);

        CustomStyling.setCustomFontToTextView(this, "big_title_gipsiero.otf", R.id.signup_title);

        //collect the reference of all of the button & editText
        this.mEdittextUsername = (EditText) findViewById(R.id.editText_username);
        this.mEdittextPassword = (EditText) findViewById(R.id.editText_password);

        this.mBtnRegister = (Button) findViewById(R.id.button_signup);
        this.mBtnLogin = (Button) findViewById(R.id.button_login);
        this.mBtnSkiplogin = (Button) findViewById(R.id.button_skip);

        this.mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                Toast.makeText(SignupLoginActivity.this, "Register clicked", Toast.LENGTH_SHORT).show();
            }
        });

        this.mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                Toast.makeText(SignupLoginActivity.this, "Login clicked", Toast.LENGTH_SHORT).show();
            }
        });

        this.mBtnSkiplogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle the register button request
                Toast.makeText(SignupLoginActivity.this, "Skip clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SignupLoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
                SignupLoginActivity.this.finish();
            }
        });
    }
}

package com.example.harikrishnan.cyc10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);
    }
    public void Loginprocess(View view)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);

    }
    public void Signup(View view)
    {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
    }
}

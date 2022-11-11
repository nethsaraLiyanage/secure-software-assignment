package com.test.abccompany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    ImageButton bt_admin,bt_manager,bt_worker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        bt_admin = findViewById(R.id.imageView2);
        bt_manager = findViewById(R.id.imageView3);
        bt_worker = findViewById(R.id.imageView4);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
        Toast.makeText(this, "Please select option ! ", Toast.LENGTH_SHORT).show();
    }

    public void adminLogIn(View view){
        Intent intent = new Intent(this,adminLogin.class);
        startActivity(intent);
    }

    public void managerLogIn(View view){
        Intent intent = new Intent(this,managerLogin.class);
        startActivity(intent);
    }

    public void workerLogIn(View view){
        Intent intent = new Intent(this,workerLogin.class);
        startActivity(intent);
    }
}



package com.test.abccompany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class managerHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(this, "Please Log Out ! ", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    public void messages(View view) {
        Intent intent = new Intent(this,managermessages.class);
        startActivity(intent);
    }

    public void uploade(View view) {
        Intent intent = new Intent(this,managerfileupload.class);
        startActivity(intent);
    }
}
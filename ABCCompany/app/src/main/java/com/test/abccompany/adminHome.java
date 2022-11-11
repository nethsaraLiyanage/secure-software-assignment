package com.test.abccompany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class adminHome extends AppCompatActivity {
    String name,post;
    TextView sad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        //name = (String) getIntent().getExtras().get("name");

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        post = sharedPreferences.getString("post","");
        sad = findViewById(R.id.paneluser);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(adminHome.this, "Please Log Out ! ", Toast.LENGTH_SHORT).show();

            }
        });

        Intent intent = new Intent(this,adminHome.class);
        startActivity(intent);
    }

    public void logout(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        name = sharedPreferences.getString("","");
        post = sharedPreferences.getString("","");
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }

    public void addmember(View view) {
        Intent intent = new Intent(this,addMember.class);
        startActivity(intent);
    }

    public void messages(View view) {
        Intent intent = new Intent(this,adminmessaages.class);
        startActivity(intent);
    }



    public void uploaded(View view) {
        Intent intent = new Intent(this,sendfiles.class);
        startActivity(intent);
    }
}
package com.test.abccompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class addMember extends AppCompatActivity {

    Spinner dropdown;
    EditText userName,password;
    String uN,pW;
    String responses,post,check;
    String bcryptHashString;
    TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        tv = findViewById(R.id.textView);
        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Manager", "Worker"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    public void home(View view) {
        Intent intent = new Intent(this,adminHome.class);
        startActivity(intent);
    }
    public void addMember(View view) {

        post = dropdown.getSelectedItem().toString();
        uN = userName.getText().toString();
        pW = password.getText().toString();
        bcryptHashString = BCrypt.withDefaults().hashToString(12,pW.toCharArray());
        if (uN.matches("") || pW.matches("")){
            if (uN.matches("") && pW.matches("")){
                Toast.makeText(this, "Please enter the username and password! ", Toast.LENGTH_SHORT).show();
            }
            else if (uN.matches("")){
                Toast.makeText(this, "Please enter the username ! ", Toast.LENGTH_SHORT).show();
            }
            else if (pW.matches("")){
                Toast.makeText(this, "Please enter the password ! ", Toast.LENGTH_SHORT).show();
            }
            userName.setText(null);
            password.setText(null);
        }
        else {
            RequestBody formBody = new FormBody.Builder()
                    .add("username", uN)
                    .add("post", post)
                    .add("password", bcryptHashString)
                    .build();

            Request request = new Request.Builder()
                    .url("https://192.168.1.165:5000/adduser")
                    .post(formBody)
                    .build();


            OkHttpClient okhttpclient = getUnsafeOkHttpClient();
            okhttpclient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(addMember.this, "Back end Server error!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    responses = response.body().string();
                    check = "added";


                    if (responses == check) {
                        added();
                    } else {
                        error();
                    }


                }
            });
        }

    }
    private void added() {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(addMember.this, "User added successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void error() {
        runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(addMember.this, "Failed to add user!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
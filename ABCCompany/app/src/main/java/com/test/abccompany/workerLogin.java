package com.test.abccompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class workerLogin extends AppCompatActivity {

    String uN,pW;
    EditText userName,password;
    String responses,validation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
    }

    public void home(View view){
        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }


    public void worker(View view){
        uN = userName.getText().toString();
        pW = password.getText().toString();
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

        else{
            String bcryptHashString = BCrypt.withDefaults().hashToString(12,pW.toCharArray());
            SharedPreferences sp=getSharedPreferences("User",MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("name",uN );
            Ed.putString("post","worker" );
            Ed.commit();
            RequestBody formBody = new FormBody.Builder()
                    .add("username", uN)
                    .add("post", "worker")
                    .build();

            Request request = new Request.Builder()
                    .url("https://192.168.1.165:5000/login")
                    .post(formBody)
                    .build();


            OkHttpClient okhttpclient = getUnsafeOkHttpClient();
            okhttpclient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Toast.makeText(workerLogin.this,"Back end Server error!",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    responses = response.body().string();
                    BCrypt.Result result = BCrypt.verifyer().verify(pW.toCharArray(), responses);
                    if (result.verified) {
                        validation = "valid";
                        workerhome();

                    } else {
                        userName.setText(null);
                        password.setText(null);


                    }

                }
            });


        }

    }

    public void workerhome(){
        Intent intent = new Intent(this,workerHome.class);
        startActivity(intent);
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
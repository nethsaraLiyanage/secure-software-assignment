package com.test.abccompany;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
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

public class adminmessaages extends AppCompatActivity {


    OkHttpClient okhttpclient,okhttpclient2;
    TextView messages;
    String responses = "",checker,secret = "M5SO8Gt0X9UHSg5YpThB0nG7hXsrpQ2v",bcryptHashString;
    Request request;
    FrameLayout back,send;
    EditText input;
    String name,messageInput = "",post,returned;
    Intent intent_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messages = findViewById(R.id.viewback);
        messages.setMovementMethod(new ScrollingMovementMethod());
        back = findViewById(R.id.layoutback);
        send = findViewById(R.id.layoutSend);
        msg();
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        post = sharedPreferences.getString("post","");
        input = findViewById(R.id.messageto);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                messageInput = input.getText().toString();
                checker = messageInput + secret;
                bcryptHashString = BCrypt.withDefaults().hashToString(12,checker.toCharArray());
                RequestBody formBody = new FormBody.Builder()
                        .add("name", name)
                        .add("post", post)
                        .add("message",messageInput)
                        .add("checker",bcryptHashString)
                        .build();
                Request request = new Request.Builder()
                        .url("https://192.168.1.165:5000/addmsg")
                        .post(formBody)
                        .build();
                okhttpclient = getUnsafeOkHttpClient();
                okhttpclient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                Toast.makeText(adminmessaages.this,"Back end Server error!",Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        responses = response.body().string();
                        runOnUiThread(new Runnable() {
                            public void run()
                            {
                                Toast.makeText(adminmessaages.this,"Message Sent Successfully!",Toast.LENGTH_SHORT).show();
                            }
                        });

                        input.setText("");
                        sendm();
                    }
                });

            }
        });

    }



    public void exit() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        name = sharedPreferences.getString("name","");
        post = sharedPreferences.getString("post","");
        Intent intent = new Intent(this,adminHome.class);
        startActivity(intent);

    }

    public void msg() {
        messages = findViewById(R.id.viewback);
        request = new Request.Builder()
                .url("https://192.168.1.165:5000/read")
                .build();


        okhttpclient = getUnsafeOkHttpClient();
        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(adminmessaages.this,"Back end Server error!",Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                responses = response.body().string();
                //test.setText(responses);
                messages.setText(responses);
            }
        });
    }

    public void sendm(){
        //messageInput = input.getText().toString();
        Intent intent = new Intent(this,adminmessaages.class);
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
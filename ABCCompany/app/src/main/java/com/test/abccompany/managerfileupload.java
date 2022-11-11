package com.test.abccompany;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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

public class managerfileupload extends AppCompatActivity {
    Button set,send,home;
    Uri fileuri;
    FirebaseStorage storage;
    Intent intent;
    String name,salt = "",pass="tYlygCrP4MT3I0cMoqcIDxMAuFXwQuPy",crypto,pass2 = "tYlygCrP4MT3I0cMoqcIDxMAuFXwQuP4",validater="",responses,post,username,userpost;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_managerfileupload);
    }

    public void upload(View view) {
        if(fileuri != null){

            name = getAlphaNumericString();
            StorageReference reference = FirebaseStorage.getInstance().getReference();
            String path = "files/"+name+".txt";
            StorageReference ref = reference.child(path);
            ref.putFile(fileuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(managerfileupload.this, "Files uploaded to server, wait while checking the connection ! ", Toast.LENGTH_SHORT).show();
                        }
                    });

                    sendData();
                }
            });


        }
    }



    private void sendData() {

        salt = getAlphaNumericString();
        validater = salt + pass2;
        crypto = salt + pass;

        String bcryptHashString = BCrypt.withDefaults().hashToString(12,crypto.toCharArray());
        RequestBody formBody = new FormBody.Builder()
                .add("salt", salt)
                .add("checker", bcryptHashString)
                .build();

        Request request = new Request.Builder()
                .url("https://192.168.1.165:5000/authserver")
                .post(formBody)
                .build();


        OkHttpClient okhttpclient = getUnsafeOkHttpClient();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(managerfileupload.this, "Server authentication failed!", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responses = response.body().string();
                validater = salt + pass2;


                BCrypt.Result result = BCrypt.verifyer().verify(validater.toCharArray(), responses);

                if (result.verified) {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(managerfileupload.this, "Downloading to the server!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    executer();


                } else {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            Toast.makeText(managerfileupload.this, "Server verification faild!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });





    }

    private void executer() {

        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        username = sharedPreferences.getString("post","");
        userpost = sharedPreferences.getString("name","");

        RequestBody formBody = new FormBody.Builder()
                .add("name", username)
                .add("post", userpost)
                .add("filename", name)
                .build();

        Request request = new Request.Builder()
                .url("https://192.168.1.165:5000/download")
                .post(formBody)
                .build();


        OkHttpClient okhttpclient = getUnsafeOkHttpClient();

        okhttpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(managerfileupload.this, "Server error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        Toast.makeText(managerfileupload.this, "File uploaded successfully!", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }


    public void homeset(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        post = sharedPreferences.getString("post","");
        Intent intent = new Intent(this, managerHome.class);
        startActivity(intent);
    }

    public void select(View view) {
        intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            fileuri = data.getData();
            runOnUiThread(new Runnable() {
                public void run()
                {
                    Toast.makeText(managerfileupload.this,"File selected successfully",Toast.LENGTH_SHORT).show();
                }
            });
        }
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

    private String getAlphaNumericString() {
        // chose a Character random from this String

        int n = 5;
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
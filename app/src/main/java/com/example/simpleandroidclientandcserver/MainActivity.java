package com.example.simpleandroidclientandcserver;

import android.app.DownloadManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.OkHttpClient;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread connect = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient.Builder builder = new OkHttpClient.Builder();
                            builder.connectTimeout(30, TimeUnit.SECONDS);
                            builder.readTimeout(30, TimeUnit.SECONDS);
                            builder.writeTimeout(30, TimeUnit.SECONDS);
//                            Socket sock = new Socket("192.168.1.131", 1234);
//                            long startTime = System.currentTimeMillis();
//                            BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//                            test = br.readLine();
//                            long difference = System.currentTimeMillis() - startTime;
//                            //Log.d("msg:", test);
//                            Log.d("time:", Long.toString(difference));
//                            sock.close();
                            OkHttpClient client = builder.build();
                            long startTime = System.currentTimeMillis();
                            Request request = new Request.Builder().
                                    url("http://35.3.60.51:8080/100 MB.file").
                                    build();
                            client.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    if (response != null) {
                                        long startTime = System.currentTimeMillis();
                                        InputStream is = response.body().byteStream();
                                        BufferedInputStream bis = new BufferedInputStream(is);
                                        long size = 0;
                                        int red = 0;
                                        byte[] buf = new byte[1024];
                                        while ((red = bis.read(buf)) != -1) {
                                            size += red;
                                        }
                                        long endTime = System.currentTimeMillis();
                                        Log.d("time:", Long.toString(endTime - startTime));
                                    }
                                }
                            });
//                            Response response = client.newCall(request).execute();
//
//                            String res = response.body().string();
//                            long difference = System.currentTimeMillis() - startTime;
//                            Log.d("time:", Long.toString(difference));
//                            //Log.d("Test:", response.body().string());
//                            Log.d("Done:", "request finished!\n");

                        } catch (Exception ex) {
                            Log.d("Error:", ex.toString());
                        }
                    }
                });
                connect.start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
